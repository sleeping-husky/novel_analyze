package text.novel

import java.util.UUID

import text.{FULL_SQUARE_BRACKET_CLOSED, FULL_SQUARE_BRACKET_OPENED, NewLineChars}
import text.meta.MetaData
import text.paragraph.{Paragraph, ParagraphData, ParagraphId, Paragraphs}
import text.sentence.Sentences
import text.word.Words
import tokenize.Tokens

case class Novel(val id: NovelId, val data: NovelData, val metaData: MetaData, tokenizer: String => Tokens, nl: NewLineChars) {

  val paragraphs: Paragraphs = toParagraphs
  val sentences: Sentences = paragraphs.toSentences(nl)
  val words: Words = sentences.toWords(tokenizer)
  val length = data.length
  def lengthAverageOneSentence: Double = sentences.lengthAverage
  def japaneseCommaAverageOneSentence: Double = sentences.japaneseCommaAverage
  def speechPercent: Double = paragraphs.speechPercent

  private def toParagraphs(): Paragraphs = {
    def openedBracketSplitBefore(value: String): Seq[String] =
      value.split(FULL_SQUARE_BRACKET_OPENED).toSeq match {
        case head +: tail => head +: tail.map(s => FULL_SQUARE_BRACKET_OPENED + s)
      }

    def closedBracketAfterSplit(value: String): Seq[String] =
      value.split(FULL_SQUARE_BRACKET_CLOSED).toSeq match {
        case Seq(head) if head.startsWith(FULL_SQUARE_BRACKET_OPENED) => Seq(head + FULL_SQUARE_BRACKET_CLOSED)
        case pre :+ last => pre.map(s => s + FULL_SQUARE_BRACKET_CLOSED) :+ last
      }

    def removeLastNL(value: String): String = if (value.endsWith(nl.value)) value.init else value
    def removeEmpty(value: String): Option[String] = if (value == "") None else Some(value)

    val paragraphs = for{
      o <- openedBracketSplitBefore(data.value)
      c <- closedBracketAfterSplit(o)
      s = removeLastNL(c)
      r <- removeEmpty(s)
      p = Paragraph(ParagraphId.newId, ParagraphData(r), id)
    } yield p
    Paragraphs(paragraphs)
  }
}
