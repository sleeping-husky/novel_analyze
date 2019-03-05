package text.word

import tokenize.{PartOfSpeech, PartOfSpeechSub}
import text.sentence.SentenceId

case class Word(wordId: WordId, base: WordBase,
           partOfSpeech: PartOfSpeech, partOfSpeechSub: PartOfSpeechSub, sentenceId: SentenceId)

case class Words(values: Seq[Word]) {

  def concat(words: Words): Words = {
    Words(values ++ words.values)
  }

  def sortedWords(wordFilter: Word => Boolean): Seq[(Int, Seq[(WordBase, Seq[SentenceId])])] = {
    values.filter(wordFilter)
      .groupBy(w => w.base)
      .toSeq
      .map(t => (t._2.size, t._1, t._2.map(w => w.sentenceId)))
      .groupBy(t => t._1)
      .toSeq
      .map(t => (t._1, t._2.map(i => (i._2, i._3))))
      .sortBy(t => t._1)
  }
}

object Words{
  def empty = Words(Seq())
}
