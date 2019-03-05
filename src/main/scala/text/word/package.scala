package text

import java.util.UUID

package object word {
  case class WordId(value: String) extends AnyVal
  object WordId {
    def newId(): WordId = WordId(UUID.randomUUID().toString)
  }

  case class WordBase(value: String) extends AnyVal
}
