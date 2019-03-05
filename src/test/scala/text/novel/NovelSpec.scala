package text.novel

import org.scalatest.{DiagrammedAssertions, FlatSpec, FunSuite}
import text.{LF, NewLineChars}
import text.meta.{AuthorName, MetaData, TitleName}
import text.paragraph.{Paragraph, ParagraphData}
import text.sentence.SentenceData
import text.word.Word
import tokenize.Tokenizer

class NovelSpec extends FunSuite with DiagrammedAssertions {

  private val META_DATA = new MetaData(TitleName("testName"), AuthorName("testAuthor"))

  test("パラグラフに変換できる") {
    val paragraphData1 = ParagraphData("あいうえお。かきくけこ。")
    val paragraphData2 = ParagraphData("「さしすせそ」")
    val novelData = NovelData(paragraphData1.value + LF.value + paragraphData2.value)
    val paragraphs = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF).paragraphs
    assert(paragraphs.values.map(m => m.data) == Seq(paragraphData1, paragraphData2))
  }

  test("文章に変換できる") {
    val sentenceData1 = SentenceData("ああああああ")
    val sentenceData2 = SentenceData("ああじはｆ")
    val sentenceData3 = SentenceData("かかああああ")
    val paragraphData1 = ParagraphData(sentenceData1.value + "。"  + sentenceData2.value + "。")
    val paragraphData2 = ParagraphData("「" + sentenceData3.value + "」")
    val novelData = NovelData(paragraphData1.value + LF.value + paragraphData2.value)
    val sentences = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF).sentences
    assert(sentences.values.map(s => s.data) == Seq(sentenceData1, sentenceData2, sentenceData3))
  }

  test("単語に変換できる") {
    val wordValue1 = "林檎"
    val wordValue2 = "apple"
    val sentenceData = SentenceData(wordValue1 + wordValue2)
    val paragraphData = ParagraphData(sentenceData.value)
    val novelData = NovelData(paragraphData.value)
    val words = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF).words
    assert(words.values.map(w => w.base.value) == Seq(wordValue1, wordValue2))
  }

  test("文字数を取得できる") {
    val paragraphData1 = ParagraphData("あいうえお。かきくけこ。")
    val paragraphData2 = ParagraphData("「さしすせそ」")
    val novelData = NovelData(paragraphData1.value + LF.value + paragraphData2.value)
    val novel = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF)
    assert(novel.length == 20)
  }

  test("一文の平均文字数を取得できる") {
    val paragraphData1 = ParagraphData("あいうえお。かきくけ。")
    val paragraphData2 = ParagraphData("「さしす」")
    val novelData = NovelData(paragraphData1.value + LF.value + paragraphData2.value)
    val novel = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF)
    assert(novel.lengthAverageOneSentence == 4)
  }

  test("一文の平均読点数を取得できる") {
    val paragraphData1 = ParagraphData("あいうえお。かき、くけこ。さし、すせ、そ")
    val paragraphData2 = ParagraphData("「たちつ、てと。なにぬねの」")
    val novelData = NovelData(paragraphData1.value + LF.value + paragraphData2.value)
    val novel = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF)
    assert(novel.japaneseCommaAverageOneSentence == 0.8)
  }

  test("セリフパーセントを取得できる") {
    val paragraphData1 = ParagraphData("あいうえおかきくけこ。さしすせそた。")
    val paragraphData2 = ParagraphData("「たちつ、てと。なにぬ」")
    val novelData = NovelData(paragraphData1.value + LF.value + paragraphData2.value)
    val novel = Novel(NovelId("aaa"), novelData, META_DATA, Tokenizer.of, LF)
    assert(novel.speechPercent == 40.0)
  }
}
