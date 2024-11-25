import Model.UnvalidatedMailAddress
import cats.data.EitherT
import cats.effect.std.Dispatcher
import cats.effect.{IO, Resource}

import scala.collection.mutable

class KeyValueStore[K, V](dispatcher: Dispatcher[IO]) {
  private val store = mutable.Map[K, V]()

  def put(key: K, value: V): EitherT[IO, String, Unit] =
    EitherT.rightT[IO, String](
      IO {
        store.put(key, value)
        ()
      }
    )
  def get(key: K): EitherT[IO, String, Option[V]] =
    EitherT.rightT[IO, String](store.get(key))
  def remove(key: K): EitherT[IO, String, Unit] =
    EitherT.rightT[IO, String](store.remove(key))
}

object ApplyForCVRegistrationWorkflowKeyValueStore {
  val dispatcherResource: Resource[IO, Dispatcher[IO]] = Dispatcher[IO]
  val UnvalidatedMailAddressStore
      : IO[KeyValueStore[String, UnvalidatedMailAddress]] =
    dispatcherResource.use { dispatcher =>
      IO(new KeyValueStore[String, UnvalidatedMailAddress](dispatcher))
    }
}
