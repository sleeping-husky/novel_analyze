package tokenize

sealed abstract class PartOfSpeech(val japaneseName: String)
object PartOfSpeech{
  private[this] val partOfSpeechMap = Seq.apply(Verb, Noun, Adjective, Adverb, Connective, Determiner, Interjection,
    PostpositionalParticle, AuxiliaryVerb, Mark, Other, Filler, Unknown).map(p => (p.japaneseName, p)).toMap
  def of(japanesePartOfSpeechName: String): PartOfSpeech = partOfSpeechMap.getOrElse(japanesePartOfSpeechName, Unknown)
}

case object Verb extends PartOfSpeech("動詞")
case object Noun extends PartOfSpeech("名詞")
case object Adjective extends PartOfSpeech("形容詞")
case object Adverb extends PartOfSpeech("副詞")
case object Connective extends PartOfSpeech("接続詞")
case object Determiner extends PartOfSpeech("連体詞")
case object Interjection extends PartOfSpeech("感動詞")
case object PostpositionalParticle extends PartOfSpeech("助詞")
case object AuxiliaryVerb extends PartOfSpeech("助動詞")
case object Mark extends PartOfSpeech("記号")
case object Other extends PartOfSpeech("その他")
case object Filler extends PartOfSpeech("フィラー")
case object Unknown extends PartOfSpeech("未知語")


/** サブの品詞、主だったもののみ用意した */
sealed abstract class PartOfSpeechSub(val japaneseName: String)
object PartOfSpeechSub{
  private[this] val partOfSpeechSubMap = Seq.apply(SuruVerbConjugation, NaiAdjectiveStem, AdjectiveVerbStem,
    ProperNoun, Suffix, Pronoun, Alphabet, SubOther).map(p => (p.japaneseName, p)).toMap
  def of(japanesePartOfSpeechSubName: String): PartOfSpeechSub = partOfSpeechSubMap.getOrElse(japanesePartOfSpeechSubName, SubOther)
}

/** サ変接続：「～する」とできる名詞 */
case object SuruVerbConjugation extends PartOfSpeechSub("サ変接続")
/** ナイ形容詞語幹：「～ない」とできる名詞 */
case object NaiAdjectiveStem extends PartOfSpeechSub("ナイ形容詞語幹")
/** 形容動詞語幹：「～な」とできる名詞 */
case object AdjectiveVerbStem extends PartOfSpeechSub("形容動詞語幹")
case object ProperNoun extends PartOfSpeechSub("固有名詞")
case object Suffix extends PartOfSpeechSub("接尾")
case object Pronoun extends PartOfSpeechSub("代名詞")
case object Alphabet extends PartOfSpeechSub("アルファベット")
case object SubOther extends PartOfSpeechSub("その他")
case object NotExists extends PartOfSpeechSub("非存在")

