package text

import java.util.UUID

package object sentence {

  case class SentenceId(value: String) extends AnyVal
  object SentenceId {
    def newId(): SentenceId = SentenceId(UUID.randomUUID().toString)
  }

  case class SentenceData(value: String) extends AnyVal {
    def length: Int = value.length
    def japaneseCommaCount: Int = value.split(FULL_JAPANESE_COMMA).length match {
      case count if count == 0 => 0
      case count => count -1
    }
  }
}
