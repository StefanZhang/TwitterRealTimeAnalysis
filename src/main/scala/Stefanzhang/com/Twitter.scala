package Stefanzhang.com

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.alibaba.fastjson.JSON
import java.sql.{Connection,DriverManager}
import SentimentAnalysisUtils.detectSentiment


object Twitter {

  def main(args: Array[String]): Unit = {

    if(args.length !=2){
      System.err.println("Usage: Twitter <db_user> <db_pass>")
      System.exit(1)
    }

    val Array(user, pass) = args
    val sparkConf = new SparkConf()//.setAppName("Twitter").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.textFileStream("hdfs://hadoop:8020/twitter_data/")

    // Helper functions
    def sourceDet(s:String): String = {

      if (s.contains("Twitter Web App")){
        return "Web app"
      }
      if (s.contains("Twitter for iPhone")){
        return "iPhone"
      }
      if (s.contains("Twitter for iPad")){
        return "iPad"
      }
      if (s.contains("Twitter for Android")){
        return "Android"
      }
      else{
        return "other"
      }
    }

    def DateFormater(s: String): String = {
      val words = s.split(" ")
      val year = words(5)
      val month = words(1)
      val day = words(2)
      val time2 = words(3)
      var m = 0
      month match {
        case "Jan" => m = 1;
        case "Feb" => m = 2;
        case "Mar" => m = 3;
        case "Apr" => m = 4;
        case "May" => m = 5;
        case "Jun" => m = 6;
        case "Jul" => m = 7;
        case "Aug" => m = 8;
        case "Sep" => m = 9;
        case "Oct" => m = 10;
        case "Nov" => m = 11;
        case "Dec" => m = 12;
      }
      return year+"/"+m+"/"+day+" "+time2
    }

    lines.foreachRDD(x=>{
      x.foreach(t=>{
        Class.forName("com.mysql.jdbc.Driver")
        val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", user, pass)
        try {
          //remove duplicates
          val sql1 = String.format("delete t1 FROM tweets t1 INNER JOIN tweets t2 WHERE t1.id < t2.id AND t1.name = t2.name;")
          conn.prepareStatement(sql1).executeUpdate()

          //parse json
          val json = JSON.parseObject(t)
          val creat_t = json.getString("created_at")
          val create = DateFormater(creat_t)
          val name = json.getJSONObject("user").getString("screen_name")
          val text_t = json.getString("text")
          val text = text_t.replaceAll("'", "")
          val source_t = json.getString("source")
          val source = sourceDet(source_t)

          println("create: "+create)
          //Sentiment Analysis
          val senti = detectSentiment(text).toString
          println("source: "+source)
          println("text"+text+", "+senti.toString)

          //insert new vals
          val sql2 = String.format("INSERT INTO twitter.tweets (time, name, text, sentiment, source) VALUES (\'%s\',\'%s\', \'%s\', \'%s\', \'%s\');",create, name, text, senti, source)
          println(sql2)
          conn.prepareStatement(sql2).executeUpdate()

        }catch {
          case e => e.printStackTrace()
        }finally {
          conn.close()
        }
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}