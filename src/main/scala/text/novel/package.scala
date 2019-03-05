package text

import java.util.UUID

package object novel {

  case class NovelId(value: String) extends AnyVal
  object NovelId {
    def newId(): NovelId = NovelId(UUID.randomUUID().toString)
  }

  case class NovelData(value: String) extends AnyVal {
    def length = value.length
  }
}
