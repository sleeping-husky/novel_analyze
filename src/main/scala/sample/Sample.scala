package sample

import java.nio.file.{Files, Path, Paths}
import java.security.{MessageDigest, Security}

import text.LF
import text.meta.{AuthorName, MetaData, TitleName}
import text.novel.{Novel, NovelData, NovelId}
import tokenize.Tokenizer

import scala.collection.JavaConverters._

object Sample {

  def main(args: Array[String]): Unit = {
    println("-- start ---------------------------------")

//    sampleOneHTML
    sampleMultipleHTML

    println("-- end ---------------------------------")
  }

  def sampleOneHTML: Unit = {

    // TODO 一時的な登録用に仮の値をいれているのでパスなどを正しく設定する必要がある
    val text = ""

    val meta = new MetaData(TitleName("test_title"), AuthorName("test_author"))
    val novel: Novel = Novel(NovelId.newId(), NovelData(text), meta, Tokenizer.of, LF)
    val dataPath: Path = Paths.get("sample_output.html")
    val html = HTMLTranslate.sampleHTML(novel)
    Files.write(dataPath, html.split("\r\n").toSeq.asJava)
  }

  def sampleMultipleHTML: Unit = {

    // TODO 一時的な登録用に仮の値をいれているのでパスなどを正しく設定する必要がある
    val parentDirPath: Path = Paths.get("parentDirPath")
        val textDirPath: Path = parentDirPath.resolve("text_file_name")
        val outputDirPath: Path = parentDirPath.resolve("output_html_name")

    val output: Path => Unit = p => {
      val text = Files.readAllLines(p).asScala.mkString("\n")
      val meta = new MetaData(TitleName("test_title"), AuthorName("test_author"))
      val novel: Novel = Novel(NovelId.newId(), NovelData(text), meta, Tokenizer.of, LF)
      val dataPath = outputDirPath.resolve(p.toFile.getName.replaceAll(".txt", ".html"))
      val html = HTMLTranslate.sampleHTML(novel)
      Files.write(dataPath, html.split("\r\n").toSeq.asJava)
    }

    textDirPath.toFile.listFiles().toSeq.map(f => f.toPath).foreach(output)
  }
}
