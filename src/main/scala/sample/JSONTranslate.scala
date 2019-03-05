package sample

import text.{CRLF, NewLineChars}
import text.novel.Novel
import text.paragraph.{Paragraph, ParagraphId}
import text.sentence.{Sentence, SentenceData, SentenceId, Sentences}
import text.word.{Word, WordBase, WordId, Words}
import tokenize.{NotExists, PartOfSpeech, Unknown}

class JSONTranslate {


}

object JSONTranslate{

  private[this] def quoted(value: String): String = "\"" + value.replaceAll("\"", "\\\"") + "\""
  def from(sentence: Sentence):String = "{" + quoted("id") + ":" + quoted(sentence.id.value) + "," +
    quoted("data") + ":" + quoted(NewLineChars.nlToBR(sentence.data.value)) + "}"
  def from(sentences: Sentences): String = "{" + quoted("sentences") +
    ":[" + sentences.values.map(s => from(s)).mkString(",") + "]}"
}
