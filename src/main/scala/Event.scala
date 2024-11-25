import Model.UnvalidatedMailAddress

sealed trait Event {
  def name: String
}

case class AppliedForCVRegistrationEvent(mailAddress: UnvalidatedMailAddress)
    extends Event {
  override def name: String = "appliedForCVRegistration"
}

case object AppliedForCVRegistrationEvent {
  def apply(mailAddress: UnvalidatedMailAddress) =
    new AppliedForCVRegistrationEvent(mailAddress)
}
