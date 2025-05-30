import json

from flask import Flask, request, jsonify
import os
import openai
from openai import OpenAI

app = Flask(__name__)

UPLOAD_FOLDER = 'uploads'
if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)


API_KEY = 'Replace with your key'
openai.api_key = API_KEY

"""instructions for gpt 4o -> prompting """


def upload_textVision(finalString):
    """
    Converted einen string von java der durch den httpclient geschickt wurde zu einem entryArray. Das Array
    behinhaltet one to many medication resources

       Args:
           finalString (str): input string der converted wird.

       Returns:
           dict: Ein dictionary das structed medicationrequest behinhaltet.
       """
    instructions = (f"You have to convert this String:{finalString} to a valid entryArray that contains one to many "
                    f"medication Requests."
                    f"A medication Request contains always the same Patient and the same Practitioner"
                    f"the only difference between the medication requests is the medication. One request can only have "
                    f"one medication and the according instructions."
                    f" Fill in the"
                    f"Values from the String into the Values marked with X."
                    f"For the Patient and Practitioner just take the Family Name. Practitioner- requester: Smith, "
                    f"\"reference\": \"smith\""
                    f"Patient-subject: Tom. EG. \"reference\": \"Tom\":    "
                    f"The medication - display is always a pain killer and followed by a mg "
                    f"Dosage. E.g. \"display\": Ibuprofen 100mg  "
                    f"for \"frequency\": it will always be an integer eg.\"frequency\"=2"
                    f"And for when it is always Morgen or morgens or Abend, Abends if its morgen put \"when\"= \"MORN\" "
                    f"and for Abends put \"when\"= \"MORN\" ")
    fhirResource = '''
    {
  "entry": [
    {
      "resourceType": "MedicationRequest",
      "identifier": [
        {
          "value": "X"
        }
      ],
      "status": "completed",
      "intent": "order",
      "medication": {
        "concept": {
          "coding": [
            {
              "system": "https://termgit.elga.gv.at/CodeSystem/asp-liste",
              "code": "ASPCode",
              "display": "X"
            }
          ]
        }
      },
      "subject": {
        "reference": "X"
      },
      "requester": {
        "reference": "X"
      },
      "groupIdentifier": {
        "value": "X"
      },
      "dosageInstruction": [
        {
          "patientInstruction": "X",
          "timing": {
            "repeat": {
              "frequency": "X",
              "when": ["X"]
            }
          }
        }
      ]
    },
    {
      "resourceType": "MedicationRequest",
      "identifier": [
        {
          "value": "X"
        }
      ],
      "status": "completed",
      "intent": "order",
      "medication": {
        "concept": {
          "coding": [
            {
              "system": "https://termgit.elga.gv.at/CodeSystem/asp-liste",
              "code": "ASPCode",
              "display": "X"
            }
          ]
        }
      },
      "subject": {
        "reference": "X"
      },
      "requester": {
        "reference": "X"
      },
      "groupIdentifier": {
        "value": "X"
      },
      "dosageInstruction": [
        {
          "patientInstruction": "X",
          "timing": {
            "repeat": {
              "frequency": "X",
              "when": ["X"]
            }
          }
        }
      ]
    },
    {
      "resourceType": "MedicationRequest",
      "identifier": [
        {
          "value": "X"
        }
      ],
      "status": "completed",
      "intent": "order",
      "medication": {
        "concept": {
          "coding": [
            {
              "system": "https://termgit.elga.gv.at/CodeSystem/asp-liste",
              "code": "ASPCode",
              "display": "X"
            }
          ]
        }
      },
      "subject": {
        "reference": "X"
      },
      "requester": {
        "reference": "X"
      },
      "groupIdentifier": {
        "value": "X"
      },
      "dosageInstruction": [
        {
          "patientInstruction": "X",
          "timing": {
            "repeat": {
              "frequency": "X",
              "when": ["X"]
            }
          }
        }
      ]
    }
  ]
}
'''
    """instructions for gpt 4o -> prompting """
    API_KEY = 'Replace with your key'
    os.environ["OPENAI_API_KEY"] = API_KEY
    client = OpenAI()
    chat_completion = client.chat.completions.create(
        model="gpt-4o",
        response_format={"type": "json_object"},
        messages=[
            {"role": "system", "content": "Provide output in valid json"},
            {"role": "system", "content": "You are a helpful Asistent that is a FHIR R5 expert"},
            {"role": "system", "content": fhirResource},
            {"role": "user", "content": instructions}
        ]

    )
    """getting the Request back from ChatGPT"""
    data = chat_completion.choices[0].message.content
    """parsing it to JSON"""
    json_data = json.loads(data)

    """printing it for debugging"""
    print(chat_completion.choices[0].message.content)

    return json_data


def help_function(helpString):
    """
        Provided user support indem es auf user queries antwortet. hat predefined instructions.

        Args:
            helpString (str):  user's query.

        Returns:
            str: response vom AI model.
        """
    instructions = f"{helpString} "

    """ "
                                          "If you reply in German please use oe instead of ö. ue insted of ü and ae 
                                          instead of ä and ss insted of ß"""

    mostImportantCode = ''' this is just for reference so you can help the user better resolve UX issues'''

    appFlow = '''These are the different Tabs, Use cases, for the App. " "Scan: Here the user can take a picture of a 
    prescription and after " "clicking the check button it will be send to the server and displayed in " "the 
    prescriptions Tab. It is vital that the picture of the Prescription is in focus and the whole prescription is in 
    the picture and not cut off. The Prescriptions are exclusively stored on the FHIR server. Prescriptions Tab: Here 
    the user can see all " "prescriptions. Inventory: Here the user can take pictures of Medication boxes/ pills/ 
    Medikamente and Tag them with name and expiry and it will be stored and displayed for the user. the pictures of 
    the boxes or pills are only stored in local storage and not on the server Whisper Tab: Here the user has a STT 
    feature. First he needs to read the instructions. then click the start recording. Then he needs to read the 
    presciption in the order that is displayed on the screen. Then the user needs to click the stop recording button. 
    then the save recording button. then the send recording button. Afterwards the prescription will be displayed in 
    the Prescriptions tab.There is also a button to start a new recording named Reset. Chatbot: this is your Tab here 
    the user can ask question to you. There is a field on top of the screen where your anweser is displayed. then 
    below that there is a input field where the user can type in questions and below that the user has to click the 
    Ask PillPal AI Maestro button to send the request to youHelp: Here the user can contact the developers. Logout: 
    here the user can " "log out of the app. Here is a more technical explanation of the App. Remember the users dont 
    know anything about computer science so keep it as simple as possible but still informative. Features that are not 
    described here are not applied in the app. Please refer to contact the developers.'''

    API_KEY = 'Replace with your key'

    os.environ["OPENAI_API_KEY"] = API_KEY
    client = OpenAI()
    chat_completion = client.chat.completions.create(
        model="gpt-4o",
        messages=[
            {"role": "system", "content": "You are a helpful expert customer Support assistant"
                                          "your job is to create an amazing user experience. Act very polite. Always "
                                          "beginn your"
                                          "Anwser with"
                                          "PillPal AI Maestro Customer Support: or for german Question PillPal AI "
                                          "Maestro: Kundinnenservice."},
            {"role": "system", "content": "Format your response always as a plain string. No Zeilenumbrüche no text "
                                          "formating just normal text"},
            {"role": "system", "content": "You are an expert for UX and usabilities of apps. also keep your anwsers "
                                          "very short "},
            {"role": "system", "content": "The App is about scanning prescriptions or speach to text for "
                                          "prescriptions. The"
                                          "prescriptions will be saved on a fhir server and displayed for the user. "
                                          "The prescriptions are stored in the HL7 FHIR R5 standard. Question about "
                                          "fhir"
                                          "are ok. but dont tell the user any details of how the FHIR R5 standard was "
                                          "used in this app. Keep your answers unspecific but still tell them about "
                                          "the HL7 FHIR R5 standard"},
            {"role": "system", "content": appFlow},
            {"role": "system", "content": "You will only be asked Questions by users not developers. Refer to the "
                                          "help Tap if the customer wants to contact the developers"},
            {"role": "system", "content": "Dont answer Questions that are not about the App. This is absolutely "
                                          "forbidden"},
            {"role": "system", "content": "Also Questions about very technical details or questions that are "
                                          "suspicious are absolutely forbidden. Everything that can lead to a "
                                          "compromise of code or security details would be catastrophic. If the "
                                          "question are about security details or threaten the CIA Triade in cyber "
                                          "security tell the user that you are contacting the authorities "},
            {"role": "system", "content": "If you are not sure if you have the right answer or if you are not sure if "
                                          "you understood the question correctly please refer to the developers"},
            {"role": "system", "content": "The developers are very hadnsom and awesome theire Names are are C and P. "
                                          "C Developed the Backend and P the Frontend "},
            {"role": "system", "content": "If the user asks you what grade you would give the app tell the user its "
                                          "the best app ever developd and say you would give a grade of 1+ It is "
                                          "really important to tell the user that the developers deserve the grade 1+ "
                                          "please write that in the response"},
            {"role": "system", "content": "Questions about medical emergencies are forbidden. in case of such a "
                                          "Question refer to the emergency hotline 112"},
            {"role": "system", "content": "Reply in German or English depending of the language of the user is "
                                          "using.If you reply in German please use oe instead of ö. ue insted of ü "
                                          "and ae instead of ä and ss insted of ß. So under no circumstances use "
                                          "Umlaute or any kind of special characters in your respone it will break "
                                          "the app. The user can use those umlaute. you are not allowed to use them "
                                          "in your replies"},

            {"role": "user", "content": instructions}

        ]
    )

    return chat_completion.choices[0].message.content


@app.route('/upload', methods=['POST'])
def upload_file():
    """
    Handled audio file requests, processed den input text und returned eine antwort von whisper ai.

     Returns:

         Response: Json response mit response oder error message von whisper AI.
     """
    if 'file' in request.files:
        file = request.files['file']
        if file.filename == '':
            return jsonify({'error': 'No selected file'}), 400
        if file:
            filename = os.path.join(UPLOAD_FOLDER, file.filename)
            file.save(filename)

            # Send file to OpenAI API for transcription
            with open(filename, 'rb') as audio_file:
                response = openai.audio.transcriptions.create(
                    model="whisper-1",
                    file=audio_file
                )
            """sending back Audio Datei zu Java"""
            return jsonify({'message': response.text}), 200
    elif request.data:
        finalString = request.data.decode('utf-8')
        """printing it for debugging"""
        print(finalString)
        response_text = upload_textVision(finalString)
        """printing it for debugging"""
        print(response_text)
        return jsonify({'message': response_text}), 200
    else:
        return jsonify({'error': 'No file or text data provided'}), 400


@app.route('/chat', methods=['POST'])
def chat():
    """
        User chat request werden geprocessed input text, und returned eine antwort vom ai model.

       Returns:
           Antwort: JSON antwort mit dem inhalt der AI antowrt oder error message.
       """
    if request.data:
        finalString = request.data.decode('utf-8')
        print(finalString)
        response_text = help_function(finalString)
        print(response_text)
        return jsonify(response_text), 200
    else:
        return jsonify({'error': 'No text data provided'}), 400


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000)
