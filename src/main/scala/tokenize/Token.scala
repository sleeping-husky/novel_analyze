package tokenize

case class Token(val baseForm: TokenBaseForm, val partOfSpeech: PartOfSpeech, val partOfSpeechSub: PartOfSpeechSub)

case class Tokens(tokens: Seq[Token]) {
  def map[A](f: Token => A): Seq[A] = tokens.map(f)
}