import Model.UnvalidatedMailAddress
import cats.effect.IO

object CommandFactory {
  def create(
      commandName: String,
      parameters: Array[String]
  ): Option[Command[_]] =
    commandName match {
      case ApplyForCVRegistrationCommand.name =>
        Some(ApplyForCVRegistrationCommand(parameters))
      case _ => None
    }
}

sealed trait Command[E <: Event] {
  def execute(): E
}

case class ApplyForCVRegistrationCommand(mailAddress: UnvalidatedMailAddress)
    extends Command[AppliedForCVRegistrationEvent] {
  override def execute(): AppliedForCVRegistrationEvent = {
    println(s"call ApplyForCVRegistrationCommand: $mailAddress")
    val program: IO[Unit] = for {
      store <-
        ApplyForCVRegistrationWorkflowKeyValueStore.unvalidatedMailAddressStore
    } yield ()
    program.unsafeRunSync()
    AppliedForCVRegistrationEvent(mailAddress)
  }
}

case object ApplyForCVRegistrationCommand {
  val name: String = "applyForCVRegistration"
  def apply(parameters: Array[String]): ApplyForCVRegistrationCommand = {
    require(parameters.length == 1)
    new ApplyForCVRegistrationCommand(UnvalidatedMailAddress(parameters.head))
  }
}
