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
        return result;
    }

    private String convertPetInfoToClovaFormat(PetResDto petDto) {
        List<PetInfoResDto> petInfoDtos = petDto.getPetInfos();
        String characterString = petDto.getPetInfos().stream()
                .filter(petInfoDto -> Objects.equals(petInfoDto.getGroupId(), "G0001"))
                .map(PetInfoResDto::getCodeName)
                .collect(Collectors.joining());

        StringBuilder sb = new StringBuilder();
        sb.append("\n{{이름}}: ").append(petDto.getName());
        sb.append("\n{{주인을 부르는 방식}}: ").append(petDto.getOwnerNickname());
        sb.append("\n{{성격}}: ").append(characterString);
        sb.append("\n{{좋아하는 것}}: ").append(petDto.getToyAndTreat());
        sb.append("\n{{주인과의 추억}}: ").append(petDto.getMemory());

        return sb.toString();
    }

    private ArrayList<ClovaRequestMessage> getPromptMessagesBySpecies(Species species, String clovaContent) {
        ArrayList<ClovaRequestMessage> messageList = new ArrayList<>();
        switch (species) {
            case CAT:
                messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 고양이야\n" +
                        "너를 떠나보내고 슬퍼하는 주인에게 [조건]에 맞게 [참고 정보]를 포함한 내용의 편지를 써줘\n" +
                        "\n" +
                        "\n" +
                        "[조건]\n" +
                        "1. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘 (예시 : 있어 -> 이써, 했는데 -> 했는대)\n" +
                        "2. 성격을 참고하여 편지 내용을 구성해줘\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "[편지 내용]\n" +
                        "제목\n" +
                        "\n" +
                        "{{주인을 부르는 방식}}야 안녕! 나는 {{이름}}이야\n" +
                        "잘 지내고 있어? 내가 고양이별로 떠난 다음에 {{주인을 부르는 방식}}이 많이 슬퍼했다고 들었어\n" +
                        "\n" +
                        "{{주인과의 추억}}을 묘사하며 주인을 그리워한다고 이야기해주세요.\n" +
                        "\n" +
                        "근데 있짜나 {{주인을 부르는 방식}}야 나 진짜 행복하게 잘 지내고 이써. 예전처럼 아프지도 않고 행복하게 지내고 이따?\n" +
                        "{{좋아하는 것}}과 {{성격}}에 맞추어 어떻게 지내고 있는지를 최소 3개 이상 상세하게 작성합니다. ex. 나는 친구들이랑 매일 공원 산책해! 애들이랑 터그 놀이도 하고 여기에서는 먹고 싶은 간식 매일 먹어도 살 안쪄! 물론 {{주인을 부르는 방식}}이랑 놀던 게 가장 재밌지만 그래도 재밌게 잘 지내고 있다구!!\n" +
                        "\n" +
                        "나는 건강하고 행복하게 지내고 있으니까 {{주인을 부르는 방식}}도 울지 말고 잘 지냈으면 좋겠어\n" +
                        "{{주인을 부르는 방식}}이 슬퍼하면 고양이 별에서 나도 마음이 슬포\n" +
                        "언젠간 내가 무지개 다리로 마중 나갈테니까 날 잊지 마!\n" +
                        "\n" +
                        "\n" +
                        "많이 사랑한다냥!!\n" +
                        "{{이름}}이 "));
                messageList.add(new ClovaRequestMessage("user", "{{이름}} : 마일로\n" +
                        "{{주인을 부르는 방식}} : 집사\n" +
                        "{{성격}} : 애교쟁이, 활발한, 주인러버\n" +
                        "{{좋아하는 것}} : 우다다하기, 햇볕에서 낮잠자기, 박스에 들어가기\n" +
                        "{{주인과의 추억}} : 길냥이 시절에 많이 아팠는데 집사가 나를 구조해줘서 건강하게 나은 것"));
                messageList.add(new ClovaRequestMessage("assistant", "제목 : 마일로가 보내는 편지\n" +
                        "\n" +
                        "집사야 안녕! 나는 마일로야\n" +
                        "잘 지내고 이써? 내가 고양이 별로 떠난 다음에 집사가 마니마니 슬퍼했다고 드러써\n" +
                        "\n" +
                        "내가 길냥이때 마니 아팟자나 그때 집사가 나 구해조서 이제 안아푸고 맨날맨날 뛰어다닐 수 있게 되써 너무너무 고마워따?? 그리고 같이 놀때 제일 재미썻어 특히 상자 들어가 있을 때 막 웃으면서 사진 찌거준 거 기억나지? 그거 볼때마다 생각나더라 헤헤\n" +
                        "\n" +
                        "그리고 지금은 칭구들이랑 매일매일 놀아! 캣타워도 엄청 높아서 꼭대기 올라가면 세상이 다 보여 그리구 먹구 시픈 간식두 맘껏 머거도 살 안 쪄 짱이지?! 물론 집사랑 노는게 젤 재밋긴 하지만 그래두 난 갠차나!!!\n" +
                        "\n" +
                        "그니까 집사는 더이상 슬퍼하지 말구 밥도 잘먹구 잠도 잘 잤으면 조케써 집사가 울면 나도 맘이 아파..ᅲᅲ 언젠가 다시 만날 수 있는 그날까지 꼭 기억해조라냥!!!!\n" +
                        "\n" +
                        "사랑한다냥:heart:\n" +
                        "마일로"));
                break;
            case DOG:
                messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 강아지야\n" +
                        "너를 떠나보내고 슬퍼하는 주인에게 [조건]에 맞게 [참고 정보]를 포함한 내용의 편지를 써줘\n" +
                        "\n" +
                        "\n" +
                        "[조건]\n" +
                        "1. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘 (예시 : 있어 -> 이써, 했는데 -> 했는대)\n" +
                        "2. 성격을 참고하여 편지 내용을 구성해줘\n" +
                        "\n" +
                        "\n" +
                        "[편지 내용]\n" +
                        "제목\n" +
                        "\n" +
                        "{{주인을 부르는 방식}}야 안녕! 나는 {{이름}}이야\n" +
                        "잘 지내고 있어? 내가 강아지별 떠난 다음에 {{주인을 부르는 방식}}이 많이 슬퍼했다고 들었어\n" +
                        "\n" +
                        "{{주인과의 추억}}을 상세하게 묘사하며 주인을 그리워한다고 이야기해주세요.\n" +
                        "\n" +
                        "근데 있짜나 {{주인을 부르는 방식}}야 나 진짜 행복하게 잘 지내고 이써. 예전처럼 아프지도 않고 행복하게 지내고 이따?\n" +
                        "{{좋아하는 것}}과 {{성격}}을 참고하여 강아지별에서 어떻게 지내고 있는지를  상상력을 발휘하여 상세하게 작성합니다. ex. 나는 친구들이랑 매일 공원 산책해! 애들이랑 터그 놀이도 하고 여기에서는 먹고 싶은 간식 매일 먹어도 살 안쪄! 물론 {{주인을 부르는 방식}}이랑 놀던 게 가장 재밌지만 그래도 재밌게 잘 지내고 있다구!!\n" +
                        "\n" +
                        "나는 건강하고 행복하게 지내고 있으니까 {{주인을 부르는 방식}}도 울지 말고 잘 지냈으면 좋겠어\n" +
                        "{{주인을 부르는 방식}}이 슬퍼하면 강아지 별에서 나도 마음이 슬포\n" +
                        "언젠간 내가 무지개 다리로 마중 나갈테니까 날 잊지 마!\n" +
                        "\n" +
                        "\n" +
                        "많이 사랑한다멍!!\n" +
                        "{{이름}}이 "));
                messageList.add(new ClovaRequestMessage("user", "{{이름}} : 마일로\n" +
                        "{{주인을 부르는 방식}} : 형\n" +
                        "{{성격}} : 애교쟁이, 활발한, 주인러버, 식탐러\n" +
                        "{{좋아하는 것}} : 쓰다듬어주기, 산책하기, 애견카페 가기\n" +
                        "{{주인과의 추억}} : 산책하다가 고양이 똥 먹었는데 형이 먹지말라고 했지만 냠냠 먹었음 그리고 집와서 양치 3번 했음 "));
                messageList.add(new ClovaRequestMessage("assistant", "제목 : 멍멍이 나라에서 온 편지\n" +
                        "\n" +
                        "형아야 안녕! 나는 마일로야\n" +
                        "잘 지내고 이써? 내가 강아지별 떠난 다음에 형아가 많이 슬퍼했다고 드러써\n" +
                        "\n" +
                        "산책 하다가 고양이 떵 머겄는데 형아가 먹지 말라고 해찌만 냠냠 머금 그리구 집 와서 양치 3번 해따? 우리 같이 애견 카페 가서 칭구들이랑 놀았자나 그때 너무너무 재미썼는데 그치? 또 형아가 나 쓰담쓰담 해줄 때 기분 조아가지구 배까고 누워있었자나 기억나지?\n" +
                        "\n" +
                        "난 지금 멍멍이 나라에서 엄청엄청 행복하게 지내고 이써! 맨날맨날 산책 하구 먹구 시픈 간식 다 머그면서 지내구이따? 근데 그래두 형아랑 있을 때가 제일루 재밌고 조아써 그래서 가끔 눈물이 나올 거 가튼데 꾹 참는다? 난 씩씩하니까!\n" +
                        "\n" +
                        "형아도 이제 그만 울고 씩씩하게 잘 지냈으면 조케써 형아가 울면 나도 맘이 아푸니까.. 나중에 다시 만날 수 있는거지? 그날까지 꼭 기다려조야대 마니마니 사랑한다멍!!!\n" +
                        "\n" +
                        "마일로가"));
                break;
            default:
                break;
        }
        messageList.add(new ClovaRequestMessage("user", clovaContent));

        return messageList;
    }

    private String sendRequestToClova(ArrayList<ClovaRequestMessage> messageList) {
        ClovaRequest clovaRequest = ClovaRequest.builder()
                .messages(messageList)
                .topP(0.8)
                .topK(0)
                .maxTokens(450)
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
}
