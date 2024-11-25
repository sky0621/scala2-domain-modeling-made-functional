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

case class ApplyForCVRegistrationCommand(request: ApplyForCVRegistrationRequest)
    extends Command[AppliedForCVRegistrationEvent] {
  override def execute(): AppliedForCVRegistrationEvent = {
    println(s"call ApplyForCVRegistrationCommand: ${request.mailAddress}")
    println("issued AppliedForCVRegistrationEvent")
    AppliedForCVRegistrationEvent(request.mailAddress)
  }
}

case object ApplyForCVRegistrationCommand {
  val name: String = "applyForCVRegistration"
  def apply(parameters: Array[String]): ApplyForCVRegistrationCommand = {
    require(parameters.length == 1)
    new ApplyForCVRegistrationCommand(
      ApplyForCVRegistrationRequest(parameters.head)
    )
  }
}
