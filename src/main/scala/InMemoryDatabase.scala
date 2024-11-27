import Model.UnvalidatedMailAddress
import cats.data.EitherT

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AppContext {
  type DBResult[A] = EitherT[Future, String, A]
}

class InMemoryDatabase[A] {
  import AppContext.DBResult

  private var storage: Map[Int, A] = Map.empty
  private val counter = new AtomicInteger(0)

  def save(value: Option[A]): DBResult[Unit] =
    EitherT {
      Future {
        value match {
          case Some(v) => {
            val id = counter.incrementAndGet()
            println(s"before save: $value")
            storage = storage + (id -> v)
            println(s"after save: $value")
            Right(())
          }
          case None => Left("No value provided to save")
        }
      }
    }

  def get(id: Int): DBResult[A] = EitherT {
    Future {
      println(s"before get: $id")
      storage.get(id).toRight(s"Record with ID $id not found")
    }
  }
}

object InMemoryDatabase {
  val unvalidatedMailAddressStorage =
    new InMemoryDatabase[UnvalidatedMailAddress]()
}
