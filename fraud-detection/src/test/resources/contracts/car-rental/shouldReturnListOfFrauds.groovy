import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "Should return a list of frauds"
    request {
        method GET()
        // remember to make a typo
        url "/frauds"
    }
    response {
        body(["marcin", "josh"])
        status 201
    }
}