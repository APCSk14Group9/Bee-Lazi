package cs300.beelazi.JSONHelper;

import android.util.JsonWriter;
import android.util.Pair;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;


public class JsonHelper {

    public JsonHelper(){

    }

    public String writeQuery(String action, String table, ArrayList<Pair<String,String>> conditions) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.beginObject();
        writer.name("action").value(action);
        writer.name("table").value(table);
        writer.name("condition");
        writeConditions(writer, conditions);
        writer.endObject();
        writer.close();
        return sw.toString();
    }

    public String writeQuery(String action, String table, ArrayList<Pair<String,String>> condition1, ArrayList<Pair<String, String>> condition2) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.beginObject();
        writer.name("action").value(action);
        writer.name("table").value(table);
        writer.name("condition");
        writeConditions(writer, condition1);
        writer.name("condition1");
        writeConditions(writer, condition2);
        writer.endObject();
        writer.close();
        return sw.toString();
    }

    public void writeConditions(JsonWriter writer, ArrayList<Pair<String,String>> conditions) throws IOException {
        writer.beginObject();
        if (conditions != null) {
            for (int i = 0; i < conditions.size(); i++) {
                writer.name(conditions.get(i).first).value(conditions.get(i).second);
            }
        }
        writer.endObject();
    }

}
