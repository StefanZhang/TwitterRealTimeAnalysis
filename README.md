[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">

  <h3 align="center">Twitter Real Time Sentiment Analysis Pipeline</h3>

  <p align="center">
    Information is crucial to the function of a democratic society where well- informed citizens can make rational political decisions. While in the past political entities were primarily utilizing newspaper and later television to inform the public, with the rise of the Internet and online social media, the political arena has transformed into a more complex structure. So, it is essential to see how people on the Internet, particularly on Twitter, think about the presidential election candidates.
  </p>
</p>



<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)



<!-- ABOUT THE PROJECT -->
## About The Project

[![Product Name Screen Shot][product-screenshot]](https://example.com)

This is the back-end implementation of [this project](https://github.com/StefanZhang/TwitterRealTimeAnalysis-Dashboard)

### Built With

* [Apache Flume 1.6.0 - CDH5.7.0](https://flume.apache.org/releases/content/1.6.0/FlumeUserGuide.html)
* [Apache Spark 2.2.0](https://spark.apache.org/releases/spark-release-2-2-0.html)
* [Apache Hadoop 2.6.0](https://hadoop.apache.org/docs/r2.6.0/)
* [Scala 2.11.8](https://www.scala-lang.org/download/2.11.8.html)
* [IntelliJ IDEA](https://www.jetbrains.com/idea/)

<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

1. VPS with CentOS, so the analysis [dashboard](https://github.com/StefanZhang/TwitterRealTimeAnalysis-Dashboard) can also work. Local VM can also be used as proof of concept.
2. Twitter API

### Installation

1. Install/configure Apache Flume and Apache Spark on VPS correctly, make sure using CDH version of Flume.
2. Make sure $FLUME_HOME/lib has the following .jar files: 
  * flume-sources-1.0-SNAPSHOT.jar [here](https://github.com/StefanZhang/TwitterRealTimeAnalysis/tree/master/jars)
  * twitter4j-stream-4.0.2.jar [here](https://github.com/StefanZhang/TwitterRealTimeAnalysis/tree/master/jars)
3. Create Flume configuration file [twitter.conf](https://github.com/StefanZhang/TwitterRealTimeAnalysis/blob/master/twitter.conf) in $FLUME_HOME/conf
4. Install IDEA and Scala plugin on your local computer.
5. Clone the project to local computer, load all the repositories on the pom.xml, and Maven build the Twitter_Flume_SparkStreaming-1.0-SNAPSHOT.jar. 

<!-- USAGE EXAMPLES -->
## Usage

1. Create new dir /root/lib on VPS, and upload the Twitter_Flume_SparkStreaming-1.0-SNAPSHOT.jar.
2. Start MySQL service
4. Create new table 'tweets' using (`create table tweets (
    id INT(100) AUTO_INCREMENT PRIMARY KEY,
    time VARCHAR(200),
    name VARCHAR(100),
    text TEXT,
    sentiment VARCHAR(30),
    source VARCHAR(100)
);`)
5. Start Flume service in $FLUME_HOME/bin, using this command:
(`nohup ./flume-ng agent \
--conf ./root/app/apache-flume-1.6.0-cdh5.7.0-bin/conf/ \
-f /root/app/apache-flume-1.6.0-cdh5.7.0-bin/conf/twitter.conf \
Dflume.root.logger=DEBUG,console -n TwitterAgent >flume.log 2>&1 &
`)
6. Start Spark Service in $SPARK_HOME/bin, using this command:
(`nohup ./spark-submit \
--class Stefanzhang.com.Twitter \
--master local[2] \
--name Twitter \
--packages org.apache.spark:spark-streaming-flume_2.11:2.2.0 \
/root/lib/Twitter_Flume_SparkStreaming-1.0-SNAPSHOT.jar tweets [DB Password] [Host IP] [Flume Port] >spark.log 2>&1 &`)


<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Xiaofeng(Stefan) Zhang - xzhang23@wpi.edu

Project Link: [https://github.com/StefanZhang/TwitterRealTimeAnalysis](https://github.com/StefanZhang/TwitterRealTimeAnalysis)



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/StefanZhang/TwitterRealTimeAnalysis.svg?style=flat-square
[contributors-url]: https://github.com/StefanZhang/TwitterRealTimeAnalysis/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/StefanZhang/TwitterRealTimeAnalysis.svg?style=flat-square
[forks-url]: https://github.com/StefanZhang/TwitterRealTimeAnalysis/network/members
[issues-shield]: https://img.shields.io/github/issues/StefanZhang/TwitterRealTimeAnalysis.svg?style=flat-square
[issues-url]: https://github.com/StefanZhang/TwitterRealTimeAnalysis/issues
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=flat-square&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/xiaofeng-stefan-zhang-26709987/
[product-screenshot]: https://i.loli.net/2020/10/23/aNpbTWYvcQZ8xLM.png
