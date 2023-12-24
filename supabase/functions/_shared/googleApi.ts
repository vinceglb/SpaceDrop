import { GoogleAPI } from "https://deno.land/x/google_deno_integration@v1.1/mod.ts";

// Create a new GoogleAPI instance
export const googleApi = new GoogleAPI({
  // The email of the service account
  email: Deno.env.get('CLIENT_EMAIL') ?? throwExpression('Missing CLIENT_EMAIL'),
  // The scope of the API you want to access
  scope: ['https://www.googleapis.com/auth/firebase.messaging'],
  // The value from the private key
  key: Deno.env.get('PRIVATE_KEY') ?? throwExpression('Missing PRIVATE_KEY'),
})

function throwExpression(errorMessage: string): never {
  throw new Error(errorMessage);
}