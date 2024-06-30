package com.example.pillpal420.backend;

import android.util.Log;

import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.documentation.LogTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class Parser {

    /**
     * Parsed die JSON Response vom FHIR R5 server um ein PatientDataModel zu erstellen
     * @param jsonResponse The JSON response string.
     * @return The PatientDataModel object.
     */
    public PatientDataModel createPatient(String jsonResponse) {
        PatientDataModel patientDataModel = null;
        Log.d(LogTag.PATIENT.name(), "Parsing JSON for getRequest");
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject patientResource;
            if (jsonObject.has("resourceType") && "Bundle".equals(jsonObject.getString("resourceType"))) {
                JSONArray entryArray = jsonObject.getJSONArray("entry");
                if (entryArray.length() > 0) {
                    JSONObject entryObject = entryArray.getJSONObject(0);
                    patientResource = entryObject.getJSONObject("resource");
                } else {
                    throw new JSONException("No entries found in the bundle");
                }
            } else {
                patientResource = jsonObject;
            }
            String id = patientResource.getString("id");
            JSONArray svnArray = patientResource.getJSONArray("identifier");
            JSONObject svnObject = svnArray.getJSONObject(0);
            String sVN = svnObject.getString("value");
            JSONArray nameArray = patientResource.getJSONArray("name");
            JSONObject nameObject = nameArray.getJSONObject(0);
            String family = nameObject.getString("family");
            JSONArray givenArray = nameObject.getJSONArray("given");
            String givenName = givenArray.getString(0);
            String prefixValue = "";
            if (nameObject.has("prefix")) {
                JSONArray prefixArray = nameObject.getJSONArray("prefix");
                prefixValue = prefixArray.getString(0);
            }
            String gender = patientResource.getString("gender");
            String birthDate = patientResource.getString("birthDate");
            JSONArray addressArray = patientResource.getJSONArray("address");
            JSONObject addressObject = addressArray.getJSONObject(0);
            JSONArray lineArray = addressObject.getJSONArray("line");
            String line = lineArray.getString(0);
            String city = addressObject.getString("city");
            String state = addressObject.getString("state");
            String postalCode = addressObject.getString("postalCode");
            String country = addressObject.getString("country");
            patientDataModel = new PatientDataModel(id, sVN, family, givenName, prefixValue, gender, birthDate, line, city, state, postalCode, country);
        } catch (JSONException e) {
            Log.d(LogTag.PATIENT.name(), "Error while parsing json for get Request");
            throw new RuntimeException(e);
        }
        return patientDataModel;
    }
    /**
     * Erstellt ein JSON Objekt um es an den FHIR R5 Server zu schicken.
     *
     * @param patientDataModel The PatientDataModelObject that will be posted to the FHIR R5 Server.
     * @return The JSON object that represents a FHIR HL7 Austria Core implementation Guide Patient Resource.
     */
    public JSONObject createPostPatientResource(PatientDataModel patientDataModel) {
        JSONObject json = new JSONObject();
        try {
            json.put("resourceType", "Patient");
            JSONArray identifierArray = new JSONArray();
            JSONObject identifierObject = new JSONObject();
            identifierObject.put("system", "urn:oid:1.2.40.0.10.1.4.3.1");
            identifierObject.put("value", patientDataModel.getIdentifierSocialSecurityNum());
            identifierArray.put(identifierObject);
            json.put("identifier", identifierArray);
            JSONObject nameObject = new JSONObject();
            nameObject.put("family", patientDataModel.getFamily());
            JSONArray givenArray = new JSONArray();
            givenArray.put(patientDataModel.getGiven());
            nameObject.put("given", givenArray);
            if (patientDataModel.getPrefix() != null && !patientDataModel.getPrefix().isEmpty()) {
                JSONArray prefixArray = new JSONArray();
                prefixArray.put(patientDataModel.getPrefix());
                nameObject.put("prefix", prefixArray);
            }
            JSONArray nameArray = new JSONArray();
            nameArray.put(nameObject);
            json.put("name", nameArray);
            json.put("gender", patientDataModel.getGender());
            json.put("birthDate", patientDataModel.getBirthDate());
            JSONObject addressObject = new JSONObject();
            JSONArray lineArray = new JSONArray();
            lineArray.put(patientDataModel.getLine());
            addressObject.put("line", lineArray);
            addressObject.put("city", patientDataModel.getCity());
            addressObject.put("state", patientDataModel.getState());
            addressObject.put("postalCode", patientDataModel.getPostalCode());
            addressObject.put("country", patientDataModel.getCountry());
            JSONArray addressArray = new JSONArray();
            addressArray.put(addressObject);
            json.put("address", addressArray);
        } catch (JSONException e) {
            Log.d(LogTag.PATIENT.name(),"Error while parsing JSON for post req");
            throw new RuntimeException(e);
        }
        Log.d(LogTag.PATIENT.name(),"JSON parsed succeffully for postReq");
        return json;
    }
    /**
     * Parsed die JSON Response vom FHIR R5 server um eine Liste von MedicationResourceDataModels zu erstellen.
     * Es wird gebraucht um die Daten für diverse Funktionen zu verwenden und sie optimal für die UI aufzubereiten.
     *
     * @param jsonResponse The response from the FHIR R5 Server that represents a ELGA eMedication MedicationRequest Resource.
     * @return List of MedicationRequestDataModel objects for further use in UI and Code.
     */
    public List<MedicationRequestDataModel> createMedicationRequest(String jsonResponse) {
        Log.d(LogTag.MEDICATION_REQUEST.name(), "Parsing json from get Request");
        List<MedicationRequestDataModel> medicationRequests = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.getString("resourceType").equals("Bundle")) {
                JSONArray entries = jsonObject.getJSONArray("entry");
                for (int i = 0; i < entries.length(); i++) {
                    JSONObject entry = entries.getJSONObject(i);
                    JSONObject resource = entry.getJSONObject("resource");
                    medicationRequests.add(parseMedicationRequest(resource));
                }
            } else {
                medicationRequests.add(parseMedicationRequest(jsonObject));
            }
        } catch (JSONException e) {
            Log.d(LogTag.MEDICATION_REQUEST.name(), "Error during Medication parsing from get request");
            throw new RuntimeException(e);
        }
        Log.d(LogTag.MEDICATION_REQUEST.name(), "Medication parsed from get request successfully");
        return medicationRequests;
    }

    /**
     * Parsed ein JSON Object um ein MedicationRequestDataModel zu erstellen.
     *
     * @param resource The JSON Object that represents the FHIR R5 medicationRequest that will be parsed to MedicationRequestDataModel.
     * @return A MedicationRequestDataModel
     * @throws JSONException JSONExeption if an error ocurs while parsing the JSON Object.
     */
    public MedicationRequestDataModel parseMedicationRequest(JSONObject resource) throws JSONException {
        String id = resource.getString("id");
        String eMedID = "";
        String eMedIDGroup = "";
        if (resource.has("identifier")) {
            JSONArray identifierArray = resource.getJSONArray("identifier");
            eMedID = identifierArray.getJSONObject(0).getString("value");
        }
        if (resource.has("groupIdentifier")) {
            eMedIDGroup = resource.getJSONObject("groupIdentifier").getString("value");
        }
        String aspCode = "";
        String displayMedication = "";
        if (resource.has("medication")) {
            JSONObject medication = resource.getJSONObject("medication");
            if (medication.has("concept")) {
                JSONObject concept = medication.getJSONObject("concept");
                if (concept.has("coding")) {
                    JSONArray codingArray = concept.getJSONArray("coding");
                    if (codingArray.length() > 0) {
                        JSONObject coding = codingArray.getJSONObject(0);
                        aspCode = coding.getString("code");
                        displayMedication = coding.getString("display");
                    }
                }
            }
        }
        String requester = "";
        if (resource.has("requester")) {
            requester = resource.getJSONObject("requester").getString("reference");
        }
        String subjectReference = "";
        if (resource.has("subject")) {
            subjectReference = resource.getJSONObject("subject").getString("reference");
        }
        List<MedicationRequestDataModel.DosageInstruction> dosageInstructions = new ArrayList<>();
        if (resource.has("dosageInstruction")) {
            JSONArray dosageArray = resource.getJSONArray("dosageInstruction");
            for (int j = 0; j < dosageArray.length(); j++) {
                JSONObject dosageObject = dosageArray.getJSONObject(j);
                String patientInstruction = dosageObject.getString("patientInstruction");
                JSONObject timing = dosageObject.getJSONObject("timing");
                JSONObject repeat = timing.getJSONObject("repeat");
                String frequency = repeat.getString("frequency");
                String when = repeat.getJSONArray("when").getString(0);
                dosageInstructions.add(new MedicationRequestDataModel.DosageInstruction(patientInstruction, frequency, when));
            }
        }
     Log.d(LogTag.MEDICATION_REQUEST.name(), "MedicationRequest parsed successfully from get");
        return new MedicationRequestDataModel(id, eMedID, eMedIDGroup, aspCode, displayMedication, requester, subjectReference, dosageInstructions);
    }

    /**
     * Erstellt ein FHIR R5 HL7 Austria eMedikation MedicationRequest resource.
     *
     * @param medicationRequest medicationRequest object that will be parsed to JSON.
     * @return JSON Object that contains a FHIR R5 medicationrequest.
     */
    public JSONObject createPostMedicationRequest(MedicationRequestDataModel medicationRequest) {
        JSONObject json = new JSONObject();
        try {
            json.put("resourceType", "MedicationRequest");
            JSONObject identifierObject = new JSONObject();
            identifierObject.put("value", medicationRequest.getIdentifiereMedID());
            JSONArray identifierArray = new JSONArray();
            identifierArray.put(identifierObject);
            json.put("identifier", identifierArray);
            JSONObject groupIdentifierObject = new JSONObject();
            groupIdentifierObject.put("value", medicationRequest.getIdentifiereMedIDGroup());
            json.put("groupIdentifier", groupIdentifierObject);
            json.put("status", "completed");
            json.put("intent", "order");
            JSONObject medication = new JSONObject();
            JSONObject concept = new JSONObject();
            JSONArray codingArray = new JSONArray();
            JSONObject coding = new JSONObject();
            coding.put("system", "https://termgit.elga.gv.at/CodeSystem/asp-liste");
            coding.put("code", medicationRequest.getAspCode());
            coding.put("display", medicationRequest.getDisplayMedication());
            codingArray.put(coding);
            concept.put("coding", codingArray);
            medication.put("concept", concept);
            json.put("medication", medication);
            json.put("requester", new JSONObject().put("reference","Practitioner/" + medicationRequest.getRequester()));
            json.put("subject", new JSONObject().put("reference","Patient/" + medicationRequest.getSubject()));
            JSONArray dosageInstructionArray = new JSONArray();
            for (MedicationRequestDataModel.DosageInstruction instruction : medicationRequest.getDosageInstructions()) {
                JSONObject dosageInstructionObject = new JSONObject();
                dosageInstructionObject.put("patientInstruction", instruction.getPatientInstruction());
                JSONObject timing = new JSONObject();
                JSONObject repeat = new JSONObject();
                repeat.put("frequency", instruction.getFrequency());
                repeat.put("when", new JSONArray().put(instruction.getWhen()));
                timing.put("repeat", repeat);
                dosageInstructionObject.put("timing", timing);
                dosageInstructionArray.put(dosageInstructionObject);
            }
            json.put("dosageInstruction", dosageInstructionArray);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.d(LogTag.MEDICATION_REQUEST.name(), "Successfully Created med req.");
        return json;
    }

    /**
     *
     * @param jsonResponse
     * @return
     */
    public PractitionerDataModel createPractitioner(String jsonResponse) {
        PractitionerDataModel practitioner = null;
        Log.d(LogTag.PRACTITIONER.getTag(), "Creating new Practitioner");
        Log.d(LogTag.PRACTITIONER.getTag(), jsonResponse);
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject practitionerResource;
            if (jsonObject.has("resourceType") && "Bundle".equals(jsonObject.getString("resourceType"))) {
                JSONArray entryArray = jsonObject.getJSONArray("entry");
                Log.d(LogTag.VISION.getTag(), "entry extracted");
                if (entryArray.length() > 0) {
                    JSONObject entryObject = entryArray.getJSONObject(0);
                    practitionerResource = entryObject.getJSONObject("resource");
                    Log.d(LogTag.VISION.getTag(), "resource extracted");
                } else {
                    Log.d(LogTag.VISION.getTag(), "No entries found in the bundle");
                    throw new JSONException("No entries found in the bundle");
                }
            } else {
                practitionerResource = jsonObject;
            }
            String id = practitionerResource.getString("id");
            JSONArray oidIdentifierArray = practitionerResource.getJSONArray("identifier");
            JSONObject oidObject = oidIdentifierArray.getJSONObject(0);
            String oidPractitioner = oidObject.getString("value");
            JSONArray nameArray = practitionerResource.getJSONArray("name");
            JSONObject nameObject = nameArray.getJSONObject(0);
            String family = nameObject.getString("family");
            JSONArray givenArray = nameObject.getJSONArray("given");
            StringBuilder givenNames = new StringBuilder();
            for (int i = 0; i < givenArray.length(); i++) {
                if (i > 0) {
                    givenNames.append(" ");
                }
                givenNames.append(givenArray.getString(i));
            }
            String suffix = "";
            if (nameObject.has("suffix")) { // better format > optional
                JSONArray suffixArray = nameObject.getJSONArray("suffix");
                StringBuilder suffixes = new StringBuilder();
                for (int i = 0; i < suffixArray.length(); i++) {
                    if (i > 0) {
                        suffixes.append(", ");
                    }
                    suffixes.append(suffixArray.getString(i));
                }
                suffix = suffixes.toString();
            }
            JSONArray telecomArray = practitionerResource.getJSONArray("telecom");
            JSONObject telecomObject = telecomArray.getJSONObject(0);
            String telecom = telecomObject.getString("value");
            JSONArray addressArray = practitionerResource.getJSONArray("address");
            JSONObject addressObject = addressArray.getJSONObject(0);
            JSONArray lineArray = addressObject.getJSONArray("line");
            StringBuilder lines = new StringBuilder();
            for (int i = 0; i < lineArray.length(); i++) {// better format > optional
                if (i > 0) {
                    lines.append(", ");
                }
                lines.append(lineArray.getString(i));
            }
            String city = addressObject.getString("city");
            String postalCode = addressObject.getString("postalCode");
            String country = addressObject.getString("country");
            Log.d(LogTag.PRACTITIONER.getTag(), id + " " + family + " " + givenNames + " " + suffix + " " + telecom + " " + lines + " " + city + " " + postalCode + " " + country);
            practitioner = new PractitionerDataModel(id, oidPractitioner, family, givenNames.toString(), suffix, telecom, lines.toString(), city, postalCode, country);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return practitioner;
    }

    /**
     *
     * @param practitioner
     * @return
     */
    public JSONObject createPostPractitionerResource(PractitionerDataModel practitioner) {
        JSONObject json = new JSONObject();
        try {
            json.put("resourceType", "Practitioner");
            JSONArray gdaIdentifierArray = new JSONArray();
            JSONObject gdaIdentifierObject = new JSONObject();
            gdaIdentifierObject.put("system","urn:ietf:rfc:3986");
            gdaIdentifierObject.put("value",practitioner.getOidPractitioner());
            gdaIdentifierArray.put(gdaIdentifierObject);
            json.put("identifier",gdaIdentifierArray);
            JSONObject nameObject = new JSONObject();
            nameObject.put("family", practitioner.getFamily());
            JSONArray givenArray = new JSONArray();
            givenArray.put(practitioner.getGiven());
            nameObject.put("given", givenArray);
            if (practitioner.getSuffix() != null && !practitioner.getSuffix().isEmpty()) {
                JSONArray suffixArray = new JSONArray();
                suffixArray.put(practitioner.getSuffix());
                nameObject.put("suffix", suffixArray);
            }
            JSONArray nameArray = new JSONArray();
            nameArray.put(nameObject);
            json.put("name", nameArray);
            if (practitioner.getTelecom() != null && !practitioner.getTelecom().isEmpty()) {
                JSONArray telecomArray = new JSONArray();
                JSONObject telecomObject = new JSONObject();
                telecomObject.put("system", "phone");
                telecomObject.put("value", practitioner.getTelecom());
                telecomObject.put("use", "work");
                telecomArray.put(telecomObject);
                json.put("telecom", telecomArray);
            }
            if (practitioner.getLine() != null && !practitioner.getLine().isEmpty()) {
                JSONArray addressArray = new JSONArray();
                JSONObject addressObject = new JSONObject();
                JSONArray lineArray = new JSONArray();
                lineArray.put(practitioner.getLine());
                addressObject.put("line", lineArray);
                addressObject.put("city", practitioner.getCity());
                addressObject.put("postalCode", practitioner.getPostalCode());
                addressObject.put("country", practitioner.getCountry());
                addressObject.put("use", "work");
                addressArray.put(addressObject);
                json.put("address", addressArray);
            }
            json.put("gender", "female");
        } catch (JSONException e) {
            Log.d(LogTag.PRACTITIONER.getTag(),"failure by parsing json for post request");
            throw new RuntimeException(e);
        }
        Log.d(LogTag.PRACTITIONER.getTag(),"json for post request successfully parsed");
        return json;
    }
}