# PillPal – FHIR-Powered Medication & Prescription App

PillPal is a mobile application developed as part of a 4th semester eHealth BSc. project at FH Joanneum. The app enables patients to manage their medications and prescriptions using HL7® FHIR® standards and modern Android architecture. 
It integrates AI-powered backend services for structured healthcare data processing. The backend is written in java for the integration in Android Studio and in Python for the GPT Wrapper. 

---

## Introduction

PillPal aims to simplify medication and prescription management through:

- Scanning physical prescriptions and converting them into structured FHIR resources
- Maintaining an inventory of medications including name, quantity, and expiry date
- Multilingual support (German, English and many more)
- Adjustable font sizes and a guided tutorial for usability. As well as a customized ChatBot for Customer support. 

The app is designed with a focus on usability for elderly or non-technical users and supports accessibility and data portability.

---

## Background and Methodology

### Medical Context

Austria shows high pharmaceutical usage per capita, yet current mobile solutions do not sufficiently support personal medication tracking. Apps like "MeineSV" and "eRezept" focus primarily on institutional needs or insurance-based access and lack private inventory features.

Studies and market analysis reveal a clear user demand for a lightweight, intuitive mobile app that supports personal medication and prescription management with interoperability features.

### Technical Implementation

Key challenges and solutions:

- **NLP** for prescription text extraction a Speach To Text feature was developed and a feature where Text was extracted Using pictures of medication boxes was also implemented. 
- **AI Wrapper**: Converts speech to text (Whisper) and text to FHIR (OpenAI GPT API)
- **FHIR Resources Used**: MedicationRequest, Patient
- **Android Best Practices**:
  - Separation of concerns with UI, Domain, and Data layers
  - Repository pattern
  - Kotlin coroutines and Flow for reactive programming

Open Health Stack was considered to enhance scalability and compliance but faced implementation challenges. If successfully integrated, it could support advanced security, modularity, and open-source scaling.

---

## Use Case and Results

### Primary Use Case

Allow users to scan and digitize prescriptions and medications. Automatically generate a personal, structured medication overview using FHIR-compatible formats.

### Achievements

- Working prototype with scan-to-FHIR and speach-to-FHIR functionality
- Functional backend pipeline for automated resource generation
- Clean architecture on Android with clearly separated concerns
- Demonstrated practical FHIR interoperability using open standards

---

## Development 

The backend logic can be found [here](https://github.com/medinterop-renner/PillPall/tree/master/app/src/main/java/com/example/pillpal420/backend). 
The Python Server can be found [here](https://github.com/medinterop-renner/PillPall/blob/master/python_server.py)
For the App to work a FHIR R4 Server is needed. A Docker Image can be found [here](https://hub.docker.com/r/hapiproject/hapi)


Implemented by:

- Constantin Renner: Backend, AI integration, FHIR server, GPT wrapper
- Paul Novak: UI design and implementation
- UX Design was completed by both Devolpers 

Architectural principles:

- Use of repositories for decoupling data access
- Application of a 3-layer model: UI, Domain, Data
- Async handling via coroutines 

---

## Dependencies

- FHIR R4 Server (e.g., HAPI FHIR)
- Python Flask for backend logic
- OpenAI Whisper for speech-to-text
- OpenAI GPT API for text-to-FHIR conversion
- Optional: Google Open Health Stack was evaluated but dismissed due to short time that was given to complete the project. 

---

## Future Work

- Integration with Google Health Stack 
- Medication reminders and alert systems
- Broader support for CDA → FHIR Mapping Language 
- Expanded accessibility features and offline support

---

## Contact and Feedback

For inquiries or support, contact:

- Email support: C. Renner 
- In-app feedback form with chat like system to help patients use the app 

---

Built as a student research project at FH Joanneum  
By: P. Novak and Constantin Renner

