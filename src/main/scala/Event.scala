import Model.UnvalidatedMailAddress

sealed trait Event {
  def name: String
}

case class AppliedForCVRegistrationEvent(
    maybeMailAddress: UnvalidatedMailAddress
) extends Event {
  override def name: String = "appliedForCVRegistration"
}

case object AppliedForCVRegistrationEvent {
  def apply(maybeMailAddress: UnvalidatedMailAddress) =
    new AppliedForCVRegistrationEvent(maybeMailAddress)
}
