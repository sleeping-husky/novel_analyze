package text.sentence

import org.scalatest.{DiagrammedAssertions, FlatSpec, FunSuite}
import text.paragraph.ParagraphId
import tokenize._

class SentenceSpec extends FunSuite with DiagrammedAssertions {


  test("読点の個数を確認できる") {
    assert(Sentence(SentenceId(""), SentenceData("aa。aaa"), ParagraphId("")).japaneseCommaCount == 0)
    assert(Sentence(SentenceId(""), SentenceData("a。aa、a。a"), ParagraphId("")).japaneseCommaCount == 1)
    assert(Sentence(SentenceId(""), SentenceData("aa。a、aaa、a"), ParagraphId("")).japaneseCommaCount == 2)
  }

  test("結合できる") {
    val sentence1 = Sentence(SentenceId("s1"), SentenceData("sd1"), ParagraphId("p1"))
    val sentences1 = Sentences(Seq(sentence1))
    val sentence2 = Sentence(SentenceId("s2"), SentenceData("sd2"), ParagraphId("p2"))
    val sentence3 = Sentence(SentenceId("s3"), SentenceData("sd3"), ParagraphId("p3"))
    val sentences2 = Sentences(Seq(sentence2, sentence3))
    val sentences = sentences1.concat(sentences2)
    assert(sentences.values == Seq(sentence1, sentence2, sentence3))
    assert(sentences.values != sentences1.values)
    assert(sentences.values != sentences2.values)
  }

  test("読点の平均個数を確認できる") {
    val sentence1 = Sentence(SentenceId("s1"), SentenceData("aa、aa"), ParagraphId("p1"))
    val sentence2 = Sentence(SentenceId("s2"), SentenceData("aa、a、aa"), ParagraphId("p2"))
    val sentence3 = Sentence(SentenceId("s3"), SentenceData("aa、、、aaa"), ParagraphId("p3"))
    assert(Sentences(Seq(sentence1, sentence2, sentence3)).japaneseCommaAverage == 2)
  }

  test("平均文字数を確認できる") {
    val sentence1 = Sentence(SentenceId("s1"), SentenceData("ああああ"), ParagraphId("p1"))
    val sentence2 = Sentence(SentenceId("s2"), SentenceData("aaaaa"), ParagraphId("p2"))
    val sentence3 = Sentence(SentenceId("s3"), SentenceData("12345a"), ParagraphId("p3"))
    assert(Sentences(Seq(sentence1, sentence2, sentence3)).lengthAverage == 5)
  }

  test("IDで取得できる") {
    val sentence1 = Sentence(SentenceId("s1"), SentenceData("ああああ"), ParagraphId("p1"))
    val sentence2 = Sentence(SentenceId("s2"), SentenceData("aaaaa"), ParagraphId("p2"))
    val sentence3 = Sentence(SentenceId("s3"), SentenceData("12345a"), ParagraphId("p3"))
    val sentences = Sentences(Seq(sentence1, sentence2, sentence3))
    assert(sentences.get(sentence1.id) == Some(sentence1))
    assert(sentences.get(SentenceId("s4")) == None)
  }

  test("wordsを作成できる") {
    val tokenizer: String => Tokens = data => {
      val token = Token(TokenBaseForm(""), Verb, SubOther)
      data match {
        case "s1" => Tokens(Seq(token))
        case "s2" => Tokens(Seq(token, token))
        case _ => Tokens(Seq.empty)
      }
    }
    val sentence1 = Sentence(SentenceId("s1"), SentenceData("s1"), ParagraphId("p1"))
    val sentence2 = Sentence(SentenceId("s2"), SentenceData("s2"), ParagraphId("p2"))
    val sentences = Sentences(Seq(sentence1, sentence2))
    assert(sentences.toWords(tokenizer).values.size == 3)
  }
}
