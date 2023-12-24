// Follow this setup guide to integrate the Deno language server with your editor:
// https://deno.land/manual/getting_started/setup_your_environment
// This enables autocomplete, go to definition, etc.

import { createClient } from "https://esm.sh/@supabase/supabase-js@2.39.1";
import { Database, Tables } from "../_shared/database.types.ts";
import { googleApi } from "../_shared/googleApi.ts";

console.log("Hello from Functions!")

Deno.serve(async (req) => {
  const json = await req.json()
  const event: Tables<'events'> = json.record

  // Create a Supabase client with the Auth context of the logged in user.
  const supabaseClient = createClient<Database>(
    // Supabase API URL - env var exported by default.
    Deno.env.get('SUPABASE_URL') ?? '',
    // Supabase API ANON KEY - env var exported by default.
    Deno.env.get('SUPABASE_ANON_KEY') ?? '',
    // Create client with Auth context of the user that called the function.
    // This way your row-level-security (RLS) policies are applied.
    { global: { headers: { Authorization: req.headers.get('Authorization')! } } }
  )

  const res = await supabaseClient.from("devices").select()
  console.log('ez res', res)

  const { data, error } = await supabaseClient
    .from("devices")
    .select()
    .eq("id", event.destination_device_id)
    .single()

  console.log('ez data', data)

  if (error) {
    console.log(error)
    return new Response(
      JSON.stringify(error),
      { headers: { "Content-Type": "application/json" } },
    )
  }

  if (data.fcm_token) {
    await sendFCM({ fcmToken: data.fcm_token, eventId: event.id })
  }

  return new Response(
    JSON.stringify(data),
    { headers: { "Content-Type": "application/json" } },
  )
})

async function sendFCM(params: { fcmToken: string, eventId: string }): Promise<void> {
  console.log("Sending FCM", params.fcmToken, params.eventId)

  // Make a request to the Google Cloud Messaging API
  const response = await googleApi.post('https://fcm.googleapis.com/v1/projects/bento-vince/messages:send', {
    message: {
      token: params.fcmToken,
      data: {
        id: params.eventId,
      },
    },
  })

  console.log(response)
}

/* To invoke locally:

  1. Run `supabase start` (see: https://supabase.com/docs/reference/cli/supabase-start)
  2. Make an HTTP request:

  curl -i --location --request POST 'http://127.0.0.1:54321/functions/v1/send-event-notification' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZS1kZW1vIiwicm9sZSI6ImFub24iLCJleHAiOjE5ODM4MTI5OTZ9.CRXP1A7WOeoJeXxjNni43kdQwgnWNReilDMblYTn_I0' \
    --header 'Content-Type: application/json' \
    --data '{"name":"Functions"}'

*/
