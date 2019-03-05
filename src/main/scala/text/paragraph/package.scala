package text

import java.util.UUID

package object paragraph {

  case class ParagraphId(value: String) extends AnyVal
  object ParagraphId {
    def newId(): ParagraphId = ParagraphId(UUID.randomUUID().toString)
  }

  case class ParagraphData(value: String) extends AnyVal {
    def length: Int = value.length
  }

  sealed trait ParagraphDivision
  /** 地の文 */
  case object Narrative extends ParagraphDivision
  /** セリフ */
  case object Speech extends ParagraphDivision
  object ParagraphDivision{
    def of(value: String): ParagraphDivision = if (value.startsWith(FULL_SQUARE_BRACKET_OPENED)) Speech else Narrative
  }
}
