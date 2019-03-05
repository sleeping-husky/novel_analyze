package text.paragraph

import org.scalatest.{DiagrammedAssertions, FunSuite}
import text.LF
import text.novel.NovelId
import text.sentence.SentenceData

class ParagraphSpec extends FunSuite with DiagrammedAssertions {

  test("区分を取得できる") {
    assert(Paragraph(ParagraphId("aa"), ParagraphData("aaaaa"), NovelId("")).division == Narrative)
    assert(Paragraph(ParagraphId("aa"), ParagraphData("「aaaaa」"), NovelId("")).division == Speech)
  }

  test("文字数を確認できる") {
    assert(Paragraph(ParagraphId("aa"), ParagraphData("aaaaa"), NovelId("")).length == 5)
  }

  test("地の文の段落を文章に変換できる") {
    val sentenceData1 = SentenceData("ああああああ")
    val sentenceData2 = SentenceData("ああじはｆ")
    val sentenceData3 = SentenceData("かかああああ")
    val paragraphData = ParagraphData(sentenceData1.value + "。"  + sentenceData2.value + "。" + LF.value + sentenceData3.value)
    val sentences = Paragraph(ParagraphId("aa"), paragraphData, NovelId("")).toSentences(LF)
    assert(sentences.values.map(s => s.data) == Seq(sentenceData1, sentenceData2, sentenceData3))
  }

  test("セリフの段落を文章に変換できる") {
    val sentenceData = SentenceData("ああああああ")
    val paragraphData = ParagraphData("「" + sentenceData.value + "」")
    val sentences = Paragraph(ParagraphId("aa"), paragraphData, NovelId("")).toSentences(LF);
    assert(sentences.values.map(s => s.data).head == sentenceData)
  }

  test("複数文章への変換が行える") {
    val novelId = NovelId("nn");
    val sentenceData1 = SentenceData("あいうえお")
    val sentenceData2 = SentenceData("かきくけこ")
    val sentenceData3 = SentenceData("さしすせそ")
    val sentenceData4 = SentenceData("たちつてと")
    val paragraph1 = Paragraph(ParagraphId("aa"), ParagraphData(sentenceData1.value + "。" + sentenceData2.value ), novelId)
    val paragraph2 = Paragraph(ParagraphId("bb"), ParagraphData("「" + sentenceData3.value + "」"), novelId)
    val paragraph3 = Paragraph(ParagraphId("cc"), ParagraphData(sentenceData4.value), novelId)
    val sentences = Paragraphs(Seq(paragraph1, paragraph2, paragraph3)).toSentences(LF)
    assert(sentences.values.map(s => s.data) == Seq(sentenceData1, sentenceData2, sentenceData3, sentenceData4))
  }

  test("セリフ割合が取得できる") {
    val novelId = NovelId("nn");
    val paragraph1 = Paragraph(ParagraphId("aa"), ParagraphData("あいうえお"), novelId)
    val paragraph2 = Paragraph(ParagraphId("bb"), ParagraphData("「かきく」"), novelId)
    val paragraph3 = Paragraph(ParagraphId("cc"), ParagraphData("さしすせそ"), novelId)
    val paragraphs = Paragraphs(Seq(paragraph1, paragraph2, paragraph3))
    assert(paragraphs.speechPercent == 33.33333333333333)
  }

  test("IDで取得できる") {
    val novelId = NovelId("nn");
    val paragraph1 = Paragraph(ParagraphId("aa"), ParagraphData("faijfaijf"), novelId)
    val paragraph2 = Paragraph(ParagraphId("bb"), ParagraphData("iooufayoa"), novelId)
    val paragraph3 = Paragraph(ParagraphId("cc"), ParagraphData("wjfaofuoay"), novelId)
    val paragraphs = Paragraphs(Seq(paragraph1, paragraph2, paragraph3))
    assert(paragraphs.get(paragraph1.id) == Some(paragraph1))
    assert(paragraphs.get(ParagraphId("fhauufa")) == None)
  }
}

