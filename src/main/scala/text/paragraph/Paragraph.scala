package text.paragraph

import java.util.UUID

import text.novel.NovelId
import text.sentence.{Sentence, SentenceData, SentenceId, Sentences}
import text.{FULL_JAPANESE_PERIOD, NewLineChars}

case class Paragraph(id: ParagraphId, data: ParagraphData, parentNovelId: NovelId) {
  val division: ParagraphDivision = ParagraphDivision.of(data.value)
  def length = data.length
  def toSentences(nl: NewLineChars): Sentences = {
    def deleteBracket(value: String): String = division match {
      case Speech => value.drop(1).dropRight(1)
      case Narrative => value
    }

    def deleteHeadNL(value: String): String = value match {
      case v if v.startsWith(nl.value) => v.replaceFirst(nl.value, "")
      case o => o
    }

    Sentences(deleteBracket(data.value).split(FULL_JAPANESE_PERIOD)
      .map(s => deleteHeadNL(s))
      .filter(s => !s.equals(nl.value))
      .map(s => Sentence(SentenceId.newId, SentenceData(s), id)))
  }
}

case class Paragraphs(values: Seq[Paragraph]) {
  private[this] lazy val map: Map[ParagraphId, Paragraph] = values.map(p => (p.id, p)).toMap
  def toSentences(nl: NewLineChars): Sentences =
    values.map(p => p.toSentences(nl)).foldLeft(Sentences.empty)((a: Sentences, b: Sentences) => a.concat(b))
  def speechPercent: Double =
    100 * (values.collect{case p if p.division == Speech => p.length}.sum.toDouble / values.map(p => p.length).sum)
  def get(paragraphId: ParagraphId): Option[Paragraph] = map.get(paragraphId)
}

object Paragraphs{
  def empty = Paragraphs(Seq())
}
