package esprit.tn.entity;

import lombok.Data;

import java.util.List;

@Data
public class Email {
    private List<String> toList;
    private String subject;
    private String content;

}
