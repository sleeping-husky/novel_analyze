package text.sentence

import java.util.UUID

import tokenize.{PartOfSpeech, Token, Tokens, Verb}
import text.paragraph.ParagraphId
import text.word._

case class Sentence(id: SentenceId, data: SentenceData, parentParagraphId: ParagraphId) {

  def length: Int = data.length
  def japaneseCommaCount = data.japaneseCommaCount

  private[this] val toWord: Token => Word =
    token => Word(WordId.newId, WordBase(token.baseForm.value), token.partOfSpeech, token.partOfSpeechSub, id)
  def toWords(tokenizer: String => Tokens): Words = Words(tokenizer.apply(data.value).map(toWord))
}

case class Sentences(values: Seq[Sentence]) {

  private[this] lazy val map: Map[SentenceId, Sentence] = values.map(s => (s.id, s)).toMap

  def toWords(tokenizer: String => Tokens): Words =
    values.map(s => s.toWords(tokenizer)).foldLeft(Words.empty)((a: Words, b: Words) => a.concat(b))
  def concat(sentences: Sentences): Sentences = Sentences(this.values ++ sentences.values)
  def lengthAverage: Double = values.map(s => s.length).sum.toDouble / values.size
  def japaneseCommaAverage: Double = values.map(s => s.japaneseCommaCount).sum.toDouble / values.size
  def get(sentenceId: SentenceId): Option[Sentence] = map.get(sentenceId)
}

object Sentences{
  def empty = Sentences(Seq())
}
