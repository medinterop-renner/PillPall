package com.example.pillpal420.backend;

import android.util.Log;

import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    public PatientDataModel createPatient(String jsonResponse) {
        PatientDataModel patientDataModel = null;
        Log.d("Testing","Creating Patient");
        try {
            // Parse the JSON response string into a JSONObject
            JSONObject jsonObject = new JSONObject(jsonResponse);

            String id = jsonObject.getString("id");

            JSONArray svnArray = jsonObject.getJSONArray("identifier");
            JSONObject svnObject = svnArray.getJSONObject(0);
            String sVN = svnObject.getString("value");


            // Get the JSONArray associated with the key "name"
            JSONArray nameArray = jsonObject.getJSONArray("name");

            // Get the first JSONObject from the "name" array
            JSONObject nameObject = nameArray.getJSONObject(0);

            // Extract values from the "name" object
            String family = nameObject.getString("family");
            JSONArray givenArray = nameObject.getJSONArray("given");
            String givenName = givenArray.getString(0);
            // "prefix" might not be present in the JSON, so we check if it exists
            String prefixValue = "";
            if (nameObject.has("prefix")) {
                JSONArray prefixArray = nameObject.getJSONArray("prefix");
                prefixValue = prefixArray.getString(0);
            }
            String gender = jsonObject.getString("gender");
            String birthDate = jsonObject.getString("birthDate");

            JSONArray addressArray = jsonObject.getJSONArray("address");
            JSONObject addressObject = addressArray.getJSONObject(0);

            JSONArray lineArray = addressObject.getJSONArray("line");
            String line = lineArray.getString(0);

            String city = addressObject.getString("city");
            String state = addressObject.getString("state");
            String postalCode = addressObject.getString("postalCode");
            String country = addressObject.getString("country");




            patientDataModel = new PatientDataModel(id,sVN, family, givenName, prefixValue,gender
            ,birthDate,line,city,state,postalCode,country);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Log.d("Testing", "Parser class PatientDataModelCreated"+patientDataModel.getId() + " " +
                patientDataModel.getIdentifierSocialSecurityNum() + " " + patientDataModel.getFamily() + " " +
                patientDataModel.getGiven() + " " + patientDataModel.getPrefix()
        +" " + patientDataModel.getGender() +" " +patientDataModel.getBirthDate() +" " +patientDataModel.getLine()
                +" " +patientDataModel.getState() + " " + patientDataModel.getPostalCode()+ " " + patientDataModel.getCountry());


        return patientDataModel;
    }
    public JSONObject createPostPatientResource(PatientDataModel patientDataModel) {
        JSONObject json = new JSONObject();
        try {
            json.put("resourceType", "Patient");

            // Adding identifier
            JSONArray identifierArray = new JSONArray();
            JSONObject identifierObject = new JSONObject();
            identifierObject.put("system", "urn:oid:1.2.40.0.10.1.4.3.1");
            identifierObject.put("value", patientDataModel.getIdentifierSocialSecurityNum());
            identifierArray.put(identifierObject);
            json.put("identifier", identifierArray);

            // Adding name
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

            // Adding gender
            json.put("gender", patientDataModel.getGender());

            // Adding birthDate
            json.put("birthDate", patientDataModel.getBirthDate());

            // Adding address
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
            throw new RuntimeException(e);
        }
        return json;
    }
    public List<MedicationRequestDataModel> createMedicationRequest(String jsonResponse) {

        Log.d("MedicationRequest", jsonResponse);
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
            Log.d("Testing", "Error during Medication parsing");
            throw new RuntimeException(e);
        }

        return medicationRequests;
    }

    public MedicationRequestDataModel parseMedicationRequest(JSONObject resource) throws JSONException {
        String id = resource.getString("id");

        // Extract eMedID and eMedIDGroup from identifier
        String eMedID = "";
        String eMedIDGroup = "";
        if (resource.has("identifier")) {
            JSONArray identifierArray = resource.getJSONArray("identifier");
            eMedID = identifierArray.getJSONObject(0).getString("value");
        }
        if (resource.has("groupIdentifier")) {
            eMedIDGroup = resource.getJSONObject("groupIdentifier").getString("value");
        }

        // Extract aspCode and displayMedication from medication.concept.coding
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

        // Extract requester reference
        String requester = "";
        if (resource.has("requester")) {
            requester = resource.getJSONObject("requester").getString("reference");
        }

        // Extract subject reference
        String subjectReference = "";
        if (resource.has("subject")) {
            subjectReference = resource.getJSONObject("subject").getString("reference");
        }

        // Extract dosage instructions
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

        return new MedicationRequestDataModel(id, eMedID, eMedIDGroup, aspCode, displayMedication, requester, subjectReference, dosageInstructions);
    }

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

            json.put("requester", new JSONObject().put("reference", medicationRequest.getRequester()));
            json.put("subject", new JSONObject().put("reference", medicationRequest.getSubject()));

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
        Log.d("MedicationRequest", "Successfully Created med req.");
        return json;
    }

    public PractitionerDataModel createPractitioner(String jsonResponse){
        PractitionerDataModel practitioner = null;

        Log.d("Practitioner","Creating new Practitioner");
        Log.d("Practitioner",jsonResponse);

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            String id = jsonObject.getString("id");

            JSONArray oidIdentifierArray = jsonObject.getJSONArray("identifier");
            JSONObject oidObject = oidIdentifierArray.getJSONObject(0);
            String oidPractitioner = oidObject.getString("value");

            // Getting Practitioner Name
            JSONArray nameArray = jsonObject.getJSONArray("name");
            JSONObject nameObject = nameArray.getJSONObject(0);

            String family = nameObject.getString("family");
            String given = nameObject.getString("given");
            String suffix = nameObject.getString("suffix");

            // Getting Practitioner digits

            JSONArray telecomArray = jsonObject.getJSONArray("telecom");
            JSONObject telecomObject = telecomArray.getJSONObject(0);

            String telecom = telecomObject.getString("value");

            // Getting Address

            JSONArray addressArray = jsonObject.getJSONArray("address");
            JSONObject addressObject = addressArray.getJSONObject(0);

            String line = addressObject.getString("line");
            String city = addressObject.getString("city");
            String postalCode = addressObject.getString("postalCode");
            String country = addressObject.getString("country");

            Log.d("Practitioner",id +" " + family+" " + given + " " + suffix+ " " +telecom+ " " +line+ " " +city+ " " +postalCode+ " " +country);

            practitioner = new PractitionerDataModel(id,oidPractitioner,family,given,suffix,telecom,line,city,postalCode,country);




        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return practitioner;
    }

    public JSONObject createPostPractitionerResource(PractitionerDataModel practitioner) {
        JSONObject json = new JSONObject();
        try {
            json.put("resourceType", "Practitioner");


            // OID hinzufügen
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

            json.put("gender", "female"); // Assuming gender is always female as per your provided resource example

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}