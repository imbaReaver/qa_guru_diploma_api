package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateResponseLombokModel {
    private String name,
            job,
            updatedAt,
            error;
}
