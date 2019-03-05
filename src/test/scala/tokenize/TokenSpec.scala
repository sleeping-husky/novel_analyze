package tokenize

import org.scalatest.{DiagrammedAssertions, FunSuite}

class TokenSpec extends FunSuite with DiagrammedAssertions {

  test("変換の確認") {
    val tokens = Tokens(Seq(Token(TokenBaseForm(""), Verb, NotExists), Token(TokenBaseForm("aa"), Noun, NotExists)))
    assert(tokens.map(t => t.partOfSpeech) == Seq(Verb, Noun))
  }
}
