import {CartDraft, CartSetCountryAction, CartUpdate} from "./generated/commercetools/models/Cart";

const cartDraft: CartDraft = {
    currency: "EUR"
}

const cartSetCurrency: CartSetCountryAction = {
    action: "setCountry",
    country: "de"
}

interface Test {
    readonly 'function': string
}

const t: Test = {
    'function': "test"
}

console.log(JSON.stringify(t))

const cartUpdate: CartUpdate = {
    actions: [ cartSetCurrency, { action: "setCustomerEmail", email: "me@example.com"}],
    version: 100
}

