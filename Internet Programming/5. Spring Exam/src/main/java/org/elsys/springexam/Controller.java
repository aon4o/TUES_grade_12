package org.elsys.springexam;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {
    final Map<Integer, Note> notes = new HashMap<>();
    int id_counter = 0;

    @PostMapping("/notes")
    public ResponseEntity<?> createNote(@RequestBody PostRequestRecord args) {
        try {

            Note note = new Note(args.text());
            notes.put(id_counter++, note);

            return ResponseEntity
                    .status(201)
                    .body(id_counter - 1);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("error");
        }
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<?> getNote(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        try {
            int idInt = Integer.parseInt(id);
            Note note = notes.get(idInt);

            GetResponseRecord response;

            if (headers.containsKey("char-case")) {
                if (Objects.equals(headers.get("char-case"), "lowercase")) {
                    response = new GetResponseRecord(idInt, note.getText().toLowerCase(Locale.ROOT));
                }
                else if (Objects.equals(headers.get("char-case"), "uppercase")) {
                    response = new GetResponseRecord(idInt, note.getText().toUpperCase(Locale.ROOT));
                }
                else {
                    response = new GetResponseRecord(idInt, note.getText());
                }
            } else {
                response = new GetResponseRecord(idInt, note.getText());
            }

            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("");
        }
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<?> editNote(@PathVariable String id, @RequestBody StringRecord args) {
        try {
            int idInt = Integer.parseInt(id);
            Note note = notes.get(idInt);
            String previous = note.getText();
            note.setText(args.text());

            return ResponseEntity
                    .ok()
                    .header("previous", previous)
                    .body(new GetResponseRecord(idInt, note.getText()));
        } catch (Exception e) {
            return ResponseEntity.status(404).body("");
        }
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable String id) {
        try {
            int idInt = Integer.parseInt(id);
            if (!notes.containsKey(idInt)) {
                throw new Exception();
            }
            notes.remove(idInt);

            return ResponseEntity.status(204).body("");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("");
        }
    }
}
