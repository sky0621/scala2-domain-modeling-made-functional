import Model.UnvalidatedMailAddress

sealed trait Request

case class ApplyForCVRegistrationRequest private (
    mailAddress: UnvalidatedMailAddress
) extends Request

case object ApplyForCVRegistrationRequest {
  def apply(mailAddress: String): ApplyForCVRegistrationRequest = {
    require(mailAddress.nonEmpty)
    new ApplyForCVRegistrationRequest(UnvalidatedMailAddress(mailAddress))
  }
}
