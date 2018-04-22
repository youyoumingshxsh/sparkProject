package xiaoshuang.spark

import org.apache.spark.sql.DataFrame
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SparkSession

/**
  * 2017/05/26
  * CDP
  */

object SparkSqlExample1 {

  case class Person(name: String, age: Long)

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark SQL basic example")
      .config("spark.some.config.option", "some-value")
      .master("local[2]")
      .getOrCreate()
    val peopleDF = spark.sparkContext
      .textFile("/home/xiaoshuang/code/sparkProject/data/people.txt")
      .map(_.split(","))
      .map(attributes => Person(attributes(0), attributes(1).trim.toInt))
    // Register the DataFrame as a temporary view
    import spark.implicits._
    peopleDF.toDF().createOrReplaceTempView("people")
    // SQL statements can be run by using the sql methods provided by Spark
    val teenagersDF = spark.sql("SELECT name, age FROM people WHERE age >19").show()

    val otherPeopleDataset = spark.createDataset(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    val otherPeople = spark.read.json(otherPeopleDataset)
    otherPeople.show()

  }
}


