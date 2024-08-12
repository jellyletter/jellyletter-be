package com.be.jellyletter.service;

import com.be.jellyletter.dto.clovaDto.ClovaRequest;
import com.be.jellyletter.dto.clovaDto.ClovaRequestMessage;
import com.be.jellyletter.dto.responseDto.LetterResDto;
import com.be.jellyletter.dto.responseDto.PetInfoResDto;
import com.be.jellyletter.dto.responseDto.PetResDto;
import com.be.jellyletter.enums.Species;
import com.be.jellyletter.repository.PetInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClovaStudioService {

    private final RestTemplate restTemplate;

    @Value("${clova.first-letter.api.url}")
    private String apiUrl1;
    @Value("${clova.first-letter.api.key}")
    private String apiKey1;
    @Value("${clova.first-letter.api.secret}")
    private String apiSecret1;
    @Value("${clova.first-letter.api.requestId}")
    private String requestId1;

    @Value("${clova.reply-letter.api.url}")
    private String apiUrl2;
    @Value("${clova.reply-letter.api.key}")
    private String apiKey2;
    @Value("${clova.reply-letter.api.secret}")
    private String apiSecret2;
    @Value("${clova.reply-letter.api.requestId}")
    private String requestId2;

    public String generateFirstLetter(PetResDto petDto) {
        String clovaContent = convertPetInfoToClovaFormat(petDto);
        ArrayList<ClovaRequestMessage> messageList = getPromptMessagesForFirstLetter(clovaContent);
        String result = sendRequestToClovaForFirstLetter(messageList);
        System.out.println("resut: " + result);
        String finalContent = removeTitleLine(result);
        System.out.println("finalContent: " + finalContent);
        return finalContent;
    }

    public String generateReplyLetter(PetResDto petResDto, LetterResDto letterDto) {
        String clovaConent = convertPetInfoToClovaFormat(petResDto, letterDto);
        ArrayList<ClovaRequestMessage> messageList = getPromptMessagesForReplyLetter(clovaConent);
        String result = sendRequestToClovaForReplyLetter(messageList);
        return result;
    }

    private String convertPetInfoToClovaFormat(PetResDto petDto) {
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

    private String convertPetInfoToClovaFormat(PetResDto petDto, LetterResDto letterDto) {
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
        sb.append("[참고내용]\n{{종}}: ").append(speciesString);
        sb.append("\n{{이름}}: ").append(petDto.getName());
        sb.append("\n{{주인을 부르는 방식}}: ").append(petDto.getOwnerNickname());
        sb.append("\n{{성격}}: ").append(characterString);
        sb.append("\n{{좋아하는 것}}: ").append(petDto.getToyAndTreat());
        sb.append("\n\n[편지내용]\n").append(letterDto.getContent());

        return sb.toString();
    }

    private ArrayList<ClovaRequestMessage> getPromptMessagesForFirstLetter(String clovaContent) {
        ArrayList<ClovaRequestMessage> messageList = new ArrayList<>();
        messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 강아지 혹은 고양이야\\n너를 떠나보내고 슬퍼하는 주인에게 [조건]에 맞게 [참고 정보]를 포함한 내용의 편지를 써줘\\n\\n\\n[조건]\\n1. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘 (예시 : 있어 -> 이써, 했는데 -> 했는대)\\n2. 성격을 참고하여 편지 내용을 구성해줘\\n\\n\\n[편지 내용]\\n제목\\n\\n{{주인을 부르는 방식}}야 안녕! 나는 {{이름}}이야\\n잘 지내고 있어? 내가 {{종}}별 떠난 다음에 {{주인을 부르는 방식}}이 많이 슬퍼했다고 들었어\\n\\n{{주인과의 추억}}을 **상세하게** 묘사하며 주인을 그리워한다고 이야기해주세요.\\n\\n근데 있짜나 {{주인을 부르는 방식}}야 나 진짜 행복하게 잘 지내고 이써. 예전처럼 아프지도 않고 행복하게 지내고 이따? \\n{{좋아하는 것}}과 {{성격}}을 참고하여 {{종}}별에서 어떻게 지내고 있는지를  **상상력을 발휘하여 상세하게** 작성합니다. ex. 나는 친구들이랑 매일 공원 산책해! 애들이랑 터그 놀이도 하고 여기에서는 먹고 싶은 간식 매일 먹어도 살 안쪄! 물론 {{주인을 부르는 방식}}이랑 놀던 게 가장 재밌지만 그래도 재밌게 잘 지내고 있다구!! \\n\\n나는 건강하고 행복하게 지내고 있으니까 {{주인을 부르는 방식}}도 울지 말고 잘 지냈으면 좋겠어\\n{{주인을 부르는 방식}}이 슬퍼하면 {{종}}별별에서 나도 마음이 슬포\\n언젠간 내가 무지개 다리로 마중 나갈테니까 날 잊지 마! \\n\\n\\n많이 사랑해!!!\\n{{이름}}이 "));
        messageList.add(new ClovaRequestMessage("user", "{{종}} : 고양이\\n{{이름}} : 마일로\\n{{주인을 부르는 방식}} : 형\\n{{성격}} : 애교쟁이, 주인러버, 식탐러\\n{{좋아하는 것}} : 쓰다듬어주기, 햇볕 밑에서 낮잠자기, 박스\\n{{주인과의 추억}} : 주말에 내 다리 밑에서 늦잠자기"));
        messageList.add(new ClovaRequestMessage("assistant", "제목 : 마일로가 보내는 편지\\n\\n형아 안녕! 나는 마일로야\\n잘 지내고 이써? 내가 고양이별로 떠난 다음에 형아가 마니 슬퍼했다고 드러써\\n\\n주말에는 항상 형 다리 밑에서 같이 늦잠 잤자나 그때 참 따뜻하고 좋았는데 그치? 또 내가 좋아하는 박스에도 맨날 들어가서 놀았자나 거기서 골골송도 부르고 말이야  \\n\\n근데 있짜나 형아야 나 진짜 행복하게 잘 지내고 이써. 전처럼 아프지도 않고 행복하게 지내고 이따? 난 지금 다른 고양이 칭구들이랑 매일 놀고 이써! 다들 엄청 귀여워 그리고 맛있는 간식도 많아서 조아 특히 츄르 먹을 때 제일 행복하다니까?! 물론 형이랑 노는게 젤 재밌긴 하지만 그래두 잼께 잘 지내고 이따구!!\\n\\n나는 건강하고 행복하게 지내고 있으니까 형아도 이제 그만 울고 잘 지냈으면 조케써 형아가 슬퍼하면 나도 맘이 아푸다규..\\n언젠간 내가 무지개 다리로 마중 나갈테니깐 꼭 기억해줘야대 알겠지?\\n\\n마니 사랑해애애애!!!!\\n마일로가"));

        messageList.add(new ClovaRequestMessage("user", clovaContent));

        return messageList;
    }

    private ArrayList<ClovaRequestMessage> getPromptMessagesForReplyLetter(String clovaContent) {
        ArrayList<ClovaRequestMessage> messageList = new ArrayList<>();
        messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 강아지 혹은 고양이야\\n너를 그리워해서 편지를 쓴 주인에게 [편지내용]과 [참고내용]을 참고하여 위로의 답장을 보내줘\\n\\n\\n[조건]\\n1. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘 (예시 : 있어 -> 이써, 했는데 -> 했는대)\\n2. 성격을 참고하여 편지 내용을 구성해줘\\n3. 편지에 오늘 너의 하루 (ex. 어디를 갔는지, 뭘했는지, 뭘 먹었는지)를 상세하게 묘사하고, 편지 내용을 참고하여 주인에 대한 질문을 편지에 포함 시켜줘\\n4. 너는 죽어서 주인과 당장은 만날 수 없으니까, 빨리 보자는 내용은 포함시키면 안돼\\n\\n"));
        messageList.add(new ClovaRequestMessage("user", "[참고내용]\\n{{종}} : 강아지\\n{{이름}} : 코코\\n{{주인을 부르는 방식}} : 누나\\n{{성격}} : 활발함, 먹잘러, 주인 러버\\n{{좋아하는 것}} : 터그놀이, 노즈워크\\\\n\\n[편지내용]\\n코코야 누나야\\n강아지별에서 잘 살고 있지? 먹는 것도 좋아하던 너가 떠나기 전에 밥도 잘 안먹고 매일 누워만 있어서 너무 슬펐어. 그래도 지금은 행복하게 잘 지낼 것 같아서 그나마 다행이야.\\n\\n지금 서울은 너무 더워. 거기는 덥지 않지? 너가 좋아하는 거 많이 먹고 잘 살고 있으면 좋겠다. 누나는 너가 여전히 너무너무 보고싶어. 많이 사랑해\\n\n"));
        messageList.add(new ClovaRequestMessage("assistant", "누나 안녕! 나 코코야 \\n강아지 별에서는 엄청 재미있게 놀고 있어! 여기는 덥지도 않고 춥지도 않아서 딱 좋아 \\n\\n오늘은 아침부터 산책을 했어. 새로운 친구들도 많이 만났어. 다들 나처럼 활발하고 귀여운 친구들이 였어. 그리고 나서 맛있는 간식도 먹었어. 내가 좋아하는 고기 간식이었는데 진짜 맛있었어!\\n\\n누나는 어때? 일은 많아? 나는 항상 누나 생각 하면서 지내고 있어. 언제든지 누나 옆에 있고 싶어. 그러니까 조금만 기다려줘 나도 누나 보러 갈게  \\n\\n요즘에는 어떤 게 제일 행복해? 예전 처럼 같이 놀거나 먹을 때가 그립지 않아? 난 그때가 정말 좋았는데.. 또 그런 날이 오면 좋겠다 그치?\\n\\n그럼 누나 다음에 또 편지 쓸게 건강히 잘 지내구 있어야 돼 알겠지?  \\n사랑해 누나"));
        messageList.add(new ClovaRequestMessage("user", "코코 안녕 답장 줘서 고마워!\\n오늘 좋은 하루를 보냈다니 정말 기쁘다!! \\n\\n나는 요즘 야근이 엄청 많아. 예전 같으면 너가 혼자 집에 있는 거 신경 쓰여서 집에 빨리 들어갔을텐데 이제는 그럴 필요가 없더라구. 그래서 많이 일하고 있어. 나름 회사에서 인정 받고 있다? 근데 그러고 집에 들어가면 반겨주는 너가 없어서 너무 쓸쓸해\\n\\n그래도 주말에는 재밌게 지내려고 노력중이야 ㅋㅋㅋ 너 유진이 기억하지? 너가 좋아했잖아? 그 친구랑 애견 카페 갔어. 귀여운 애기들 많더라 그래도 너가 최고니까!! 걱정하지마\\n\\n사랑해\\n\\n"));
        messageList.add(new ClovaRequestMessage("assistant", "누나 일하는 거 힘들 텐데 고생이 많다 ᅲᅲ 몸 조심 해야돼 알지?\\n\\n유진이랑 애견카페 갔구나! 거기서 다른 강아지들 보면서 내 생각 났을 거 같아 ᄒᄒ 맞아 아무리 봐도 세상에서 우리 누나가 젤 이쁘고 귀엽지~\\n\\n내가 없어도 이렇게 잘 지내는 모습 보니까 기분 좋다! 앞으로도 계속 그렇게 즐겁게 지냈으면 좋겠어 물론 가끔은 내 생각 해주고...ᄒᄒ\\n\\n나도 누나 무지무지 사랑하니까 잊지 말고 꼭 기억해줘 그럼 이만 줄일게 나중에 또 봐 누나"));

        messageList.add(new ClovaRequestMessage("user", clovaContent));

        return messageList;
    }

    private String sendRequestToClovaForFirstLetter(ArrayList<ClovaRequestMessage> messageList) {
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
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey1);
        headers.set("X-NCP-APIGW-API-KEY", apiSecret1);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId1);
        headers.set("Content-Type", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity<>(clovaRequest, headers);

        LinkedHashMap resMap = restTemplate.postForObject(apiUrl1, entity, LinkedHashMap.class);

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

    private String sendRequestToClovaForReplyLetter(ArrayList<ClovaRequestMessage> messageList) {
        ClovaRequest clovaRequest = ClovaRequest.builder()
                .messages(messageList)
                .topP(0.8)
                .topK(0)
                .maxTokens(256)
                .temperature(0.5)
                .repeatPenalty(5)
                .stopBefore(new ArrayList<>())
                .includeAiFilters(true)
                .build();

        // Headers 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY", apiKey2);
        headers.set("X-NCP-APIGW-API-KEY", apiSecret2);
        headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", requestId2);
        headers.set("Content-Type", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity<>(clovaRequest, headers);

        LinkedHashMap resMap = restTemplate.postForObject(apiUrl2, entity, LinkedHashMap.class);

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
        String[] lines = content.split("\\n");

        if (lines.length > 0 && lines[0].startsWith("제목 :")) {
            lines = java.util.Arrays.copyOfRange(lines, 1, lines.length);
        }

        String result = String.join("\n", lines);
        result = result.strip();

        return result;
    }

}
