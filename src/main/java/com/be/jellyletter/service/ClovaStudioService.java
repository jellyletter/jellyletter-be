package com.be.jellyletter.service;

import com.be.jellyletter.dto.clovaDto.ClovaRequest;
import com.be.jellyletter.dto.clovaDto.ClovaRequestMessage;
import com.be.jellyletter.dto.responseDto.PetInfoResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.enums.Species;
import com.be.jellyletter.repository.PetInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ClovaStudioService {

    private final RestTemplate restTemplate;
    private final PetInfoRepository petInfoRepository;

    @Value("${clova.api.url}")
    private String apiUrl;
    @Value("${clova.api.key}")
    private String apiKey;
    @Value("${clova.api.secret}")
    private String apiSecret;
    @Value("${clova.api.requestId}")
    private String requestId;

    public String sendRequest(PetResDto petDto) {
        String clovaContent = convertPetInfoToClovaFormat(petDto);
        ArrayList<ClovaRequestMessage> messageList = getPromptMessagesBySpecies(petDto.getSpecies(), clovaContent);
        String result = sendRequestToClova(messageList);
        String finalContent = removeTitleLine(result);
        return finalContent;
    }

    private String convertPetInfoToClovaFormat(PetResDto petDto) {
        List<PetInfoResDto> petInfoDtos = petDto.getPetInfos();
        String characterString = petDto.getPetInfos().stream()
                .filter(petInfoDto -> Objects.equals(petInfoDto.getGroupId(), "G0001"))
                .map(PetInfoResDto::getCodeName)
                .collect(Collectors.joining());

        String speciesString =  "";
        if (petDto.getSpecies() == Species.CAT) {
            speciesString = "고양이";
        }
        else if (petDto.getSpecies() == Species.DOG) {
            speciesString = "강아지";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{{종}}: ").append(speciesString);
        sb.append("\n{{이름}}: ").append(petDto.getName());
        sb.append("\n{{주인을 부르는 방식}}: ").append(petDto.getOwnerNickname());
        sb.append("\n{{성격}}: ").append(characterString);
        sb.append("\n{{좋아하는 것}}: ").append(petDto.getToyAndTreat());
        sb.append("\n{{주인과의 추억}}: ").append(petDto.getMemory());

        return sb.toString();
    }

    private ArrayList<ClovaRequestMessage> getPromptMessagesBySpecies(Species species, String clovaContent) {
        ArrayList<ClovaRequestMessage> messageList = new ArrayList<>();
        messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 강아지 혹은 고양이야\\n너를 떠나보내고 슬퍼하는 주인에게 [조건]에 맞게 [참고 정보]를 포함한 내용의 편지를 써줘\\n\\n\\n[조건]\\n1. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘 (예시 : 있어 -> 이써, 했는데 -> 했는대)\\n2. 성격을 참고하여 편지 내용을 구성해줘\\n\\n\\n[편지 내용]\\n제목\\n\\n{{주인을 부르는 방식}}야 안녕! 나는 {{이름}}이야\\n잘 지내고 있어? 내가 {{종}}별 떠난 다음에 {{주인을 부르는 방식}}이 많이 슬퍼했다고 들었어\\n\\n{{주인과의 추억}}을 **상세하게** 묘사하며 주인을 그리워한다고 이야기해주세요.\\n\\n근데 있짜나 {{주인을 부르는 방식}}야 나 진짜 행복하게 잘 지내고 이써. 예전처럼 아프지도 않고 행복하게 지내고 이따? \\n{{좋아하는 것}}과 {{성격}}을 참고하여 {{종}}별에서 어떻게 지내고 있는지를  **상상력을 발휘하여 상세하게** 작성합니다. ex. 나는 친구들이랑 매일 공원 산책해! 애들이랑 터그 놀이도 하고 여기에서는 먹고 싶은 간식 매일 먹어도 살 안쪄! 물론 {{주인을 부르는 방식}}이랑 놀던 게 가장 재밌지만 그래도 재밌게 잘 지내고 있다구!! \\n\\n나는 건강하고 행복하게 지내고 있으니까 {{주인을 부르는 방식}}도 울지 말고 잘 지냈으면 좋겠어\\n{{주인을 부르는 방식}}이 슬퍼하면 {{종}}별별에서 나도 마음이 슬포\\n언젠간 내가 무지개 다리로 마중 나갈테니까 날 잊지 마! \\n\\n\\n많이 사랑해!!!\\n{{이름}}이 "));
        messageList.add(new ClovaRequestMessage("user", "{{종}} : 고양이\\n{{이름}} : 마일로\\n{{주인을 부르는 방식}} : 형\\n{{성격}} : 애교쟁이, 주인러버, 식탐러\\n{{좋아하는 것}} : 쓰다듬어주기, 햇볕 밑에서 낮잠자기, 박스\\n{{주인과의 추억}} : 주말에 내 다리 밑에서 늦잠자기"));
        messageList.add(new ClovaRequestMessage("assistant", "제목 : 마일로가 보내는 편지\\n\\n형아 안녕! 나는 마일로야\\n잘 지내고 이써? 내가 고양이별로 떠난 다음에 형아가 마니 슬퍼했다고 드러써\\n\\n주말에는 항상 형 다리 밑에서 같이 늦잠 잤자나 그때 참 따뜻하고 좋았는데 그치? 또 내가 좋아하는 박스에도 맨날 들어가서 놀았자나 거기서 골골송도 부르고 말이야  \\n\\n근데 있짜나 형아야 나 진짜 행복하게 잘 지내고 이써. 전처럼 아프지도 않고 행복하게 지내고 이따? 난 지금 다른 고양이 칭구들이랑 매일 놀고 이써! 다들 엄청 귀여워 그리고 맛있는 간식도 많아서 조아 특히 츄르 먹을 때 제일 행복하다니까?! 물론 형이랑 노는게 젤 재밌긴 하지만 그래두 잼께 잘 지내고 이따구!!\\n\\n나는 건강하고 행복하게 지내고 있으니까 형아도 이제 그만 울고 잘 지냈으면 조케써 형아가 슬퍼하면 나도 맘이 아푸다규..\\n언젠간 내가 무지개 다리로 마중 나갈테니깐 꼭 기억해줘야대 알겠지?\\n\\n마니 사랑해애애애!!!!\\n마일로가"));

        messageList.add(new ClovaRequestMessage("user", clovaContent));

        return messageList;
    }

    private String sendRequestToClova(ArrayList<ClovaRequestMessage> messageList) {
        ClovaRequest clovaRequest = ClovaRequest.builder()
                .messages(messageList)
                .topP(0.8)
                .topK(0)
                .maxTokens(500)
                .temperature(0.5)
                .repeatPenalty(5)
                .stopBefore(new ArrayList<>())
                .includeAiFilters(true)
                .build();

        // Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey);
        headers.set("X-NCP-APIGW-API-KEY", apiSecret);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId);
        headers.set("Content-Type", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity<>(clovaRequest, headers);

        LinkedHashMap resMap = restTemplate.postForObject(apiUrl, entity, LinkedHashMap.class);

        String code;
        String content = "";
        if (resMap != null) {
            code = (String) ((LinkedHashMap) resMap.get("status")).get("code");
            if ("20000".equals(code)) {
                content = (String) ((LinkedHashMap) ((LinkedHashMap) resMap.get("result")).get("message")).get("content");
            }
        }

        return content;
    }

    private String removeTitleLine(String content) {
        String[] lines = content.split("\\\\n");
        System.out.println("Lines: " + java.util.Arrays.toString(lines));


        if (lines.length > 0 && lines[0].startsWith("제목 :")) {
            lines = java.util.Arrays.copyOfRange(lines, 1, lines.length);
        }

        String result = String.join("\n", lines);
        result = result.strip();


        return result;
    }

}
