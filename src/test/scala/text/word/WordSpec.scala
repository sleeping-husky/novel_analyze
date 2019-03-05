package text.word

import org.scalatest.{DiagrammedAssertions, FlatSpec, FunSuite}
import text.sentence.SentenceId
import tokenize.{NotExists, Verb}

class WordSpec extends FunSuite with DiagrammedAssertions {

  test("結合の確認") {
    val word1 = Word(WordId("w1"), WordBase("wb1"), Verb, NotExists, SentenceId("s1"))
    val words1 = Words(Seq(word1))
    val word2 = Word(WordId("w2"), WordBase("wb2"), Verb, NotExists, SentenceId("s2"))
    val word3 = Word(WordId("w3"), WordBase("wb3"), Verb, NotExists, SentenceId("s3"))
    val words2 = Words(Seq(word2, word3))
    val words = words1.concat(words2)
    assert(words.values == Seq(word1, word2, word3))
    assert(words.values != words1.values)
    assert(words.values != words2.values)
  }

  test("出現回数順のシーケンスで並べる") {
    val words = Words(Seq(
      Word(WordId("w1"), WordBase("abcdefg"), Verb, NotExists, SentenceId("s1")),
      Word(WordId("w2"), WordBase("abcdefg"), Verb, NotExists, SentenceId("s2")),
      Word(WordId("w3"), WordBase("1234567"), Verb, NotExists, SentenceId("s3")),
      Word(WordId("w4"), WordBase("a"), Verb, NotExists, SentenceId("s4"))))
    val sortedWords = words.sortedWords(w => w.base.value.length != 1)
    assert(sortedWords.size == 2)
    assert(sortedWords(0)._1 == 1)
    assert(sortedWords(1)._1 == 2)
    assert(sortedWords(0)._2(0)._1.value == "1234567")
    assert(sortedWords(1)._2(0)._1.value == "abcdefg")
    assert(sortedWords(1)._2(0)._2(0).value == "s1")
    assert(sortedWords(1)._2(0)._2(1).value == "s2")
  }
}
