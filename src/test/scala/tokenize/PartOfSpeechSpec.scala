package tokenize

import org.scalatest.{DiagrammedAssertions, FlatSpec, FunSuite}

class PartOfSpeechSpec extends FunSuite with DiagrammedAssertions {

  test("partOfSpeechの日本語名から品詞が取得できる") {
    assert(PartOfSpeech.of("動詞") == Verb)
    assert(PartOfSpeech.of("名詞") == Noun)
    assert(PartOfSpeech.of("形容詞") == Adjective)
    assert(PartOfSpeech.of("副詞") == Adverb)
    assert(PartOfSpeech.of("接続詞") == Connective)
    assert(PartOfSpeech.of("連体詞") == Determiner)
    assert(PartOfSpeech.of("感動詞") == Interjection)
    assert(PartOfSpeech.of("助詞") == PostpositionalParticle)
    assert(PartOfSpeech.of("助動詞") == AuxiliaryVerb)
    assert(PartOfSpeech.of("記号") == Mark)
    assert(PartOfSpeech.of("その他") == Other)
    assert(PartOfSpeech.of("フィラー") == Filler)
    assert(PartOfSpeech.of("未知語") == Unknown)
    assert(PartOfSpeech.of("") == Unknown)
  }

  test("partOfSpeechSubの日本語名から品詞のサブ情報が取得できる") {
    assert(PartOfSpeechSub.of("サ変接続") == SuruVerbConjugation)
    assert(PartOfSpeechSub.of("ナイ形容詞語幹") == NaiAdjectiveStem)
    assert(PartOfSpeechSub.of("形容動詞語幹") == AdjectiveVerbStem)
    assert(PartOfSpeechSub.of("固有名詞") == ProperNoun)
    assert(PartOfSpeechSub.of("接尾") == Suffix)
    assert(PartOfSpeechSub.of("代名詞") == Pronoun)
    assert(PartOfSpeechSub.of("アルファベット") == Alphabet)
    assert(PartOfSpeechSub.of("その他") == SubOther)
    assert(PartOfSpeechSub.of("") == SubOther)
  }
}
