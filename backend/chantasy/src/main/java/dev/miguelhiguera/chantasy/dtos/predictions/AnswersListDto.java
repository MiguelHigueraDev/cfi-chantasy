package dev.miguelhiguera.chantasy.dtos.predictions;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswersListDto {

    @Valid
    @NotEmpty(message = "La lista de respuestas no puede estar vacía.")
    private List<AnswerDto> answers;
}
