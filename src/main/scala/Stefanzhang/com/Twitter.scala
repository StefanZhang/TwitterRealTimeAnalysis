package Stefanzhang.com

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import com.alibaba.fastjson.JSON

object Twitter {

  def main(args: Array[String]): Unit = {

    if(args.length !=2){
      System.err.println("Usage: Twitter <hostname> <port>")
      System.exit(1)
    }

    val Array(hostname, port) = args
    val sparkConf = new SparkConf().setAppName("Twitter").setMaster("local[2]")
    val ssc = new StreamingContext(sparkConf, Seconds(5))

    val lines = ssc.textFileStream("hdfs://172.16.44.128:8020/twitter_data/")
    // VM : 172.16.44.128

    lines.foreachRDD(x=>{
      x.foreach(t=>{
        //Class.forName("com.mysql.jdbc.Driver")
        //val conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Twitter", "root", "123456")
        //println(t)
        try {
          val json = JSON.parseObject(t)
          val create = json.getString("created_at")
          val name = json.getJSONObject("user").getString("name")
          val sql = String.format("insert into spark values(\"%s\",\"%s\")",create,name)
          println(name)
          //conn.prepareStatement(sql).executeUpdate()
        }catch {
          case e => e.printStackTrace()
        }finally {
          //conn.close()
        }
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }
}