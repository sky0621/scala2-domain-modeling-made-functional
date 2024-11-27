import Model.UnvalidatedMailAddress

object CommandFactory {
  def create(
      commandName: String,
      parameters: Array[String]
  ): Command[_] =
    commandName match {
      case ApplyForCVRegistrationCommand.name =>
        ApplyForCVRegistrationCommand(parameters)
      case _ => throw new RuntimeException()
    }
}

sealed trait Command[E <: Event] {
  def execute(): E
}

case class ApplyForCVRegistrationCommand(
    maybeMailAddress: UnvalidatedMailAddress
) extends Command[AppliedForCVRegistrationEvent] {
  override def execute(): AppliedForCVRegistrationEvent = {
    println(s"call ApplyForCVRegistrationCommand: $maybeMailAddress")
    val mailOpt: Option[UnvalidatedMailAddress] =
      if (maybeMailAddress.value.isBlank) None else Some(maybeMailAddress)
    InMemoryDatabase.unvalidatedMailAddressStorage.save(mailOpt)
    println("before create event")
    AppliedForCVRegistrationEvent(maybeMailAddress)
  }
}

case object ApplyForCVRegistrationCommand {
  val name: String = "applyForCVRegistration"
  def apply(parameters: Array[String]): ApplyForCVRegistrationCommand = {
    require(parameters.length == 1)
    new ApplyForCVRegistrationCommand(
      UnvalidatedMailAddress(parameters.head)
    )
  }
}
