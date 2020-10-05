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
    // VM : 172.16.44.128

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
          val create = json.getString("created_at")
          val name = json.getJSONObject("user").getString("screen_name")
          val text = json.getString("text").replace("'", "''")
          val source = json.getString("source")

          //Sentiment Analysis
          val senti = detectSentiment(text).toString
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