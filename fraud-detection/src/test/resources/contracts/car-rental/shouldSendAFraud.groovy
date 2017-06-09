import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should send a fraud"
    label "trigger_a_fraud"
    input {
        triggeredBy "triggerMethod()"
    }
    outputMessage {
        // TYPO!
        sentTo "frauds"
        body(surname: "Long")
    }
}