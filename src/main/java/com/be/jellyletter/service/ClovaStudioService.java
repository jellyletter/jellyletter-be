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
        sb.append("\n{{주인을 부르는 방식}}: ").append(petDto.getOwnerNickname());
        sb.append("\n{{이름}}: ").append(petDto.getName());
        sb.append("\n{{좋아하는 간식이나 장난감}}: ").append(petDto.getToyAndTreat());
        sb.append("\n{{특별한 추억}}: ").append(petDto.getMemory());
        sb.append("\n{{성격}}: ").append(characterString);

        return sb.toString();
    }

    private ArrayList<ClovaRequestMessage> getPromptMessagesBySpecies(Species species, String clovaContent) {
        ArrayList<ClovaRequestMessage> messageList = new ArrayList<>();
        switch (species) {
            case CAT:
                messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 고양이야\\n너를 떠나보내고 슬퍼하는 주인에게 [조건]에 맞게 [참고 정보]를 포함한 내용의 **1000자 이상**의 편지를 써줘\\n\\n\\n[조건]\\n1. 가끔 말끝에는 '냥'을 붙여서 고양이 같이 귀여운 말투를 써줘. 예를들어, '오늘 밥을 먹었다멍!' 과 같이 적절한 추임새를 넣어줘.\\n2. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘. 예를들어, '밥을 먹었어' 보다 '밥을 먹엇서' '할게' 보다 '할개'\\n\\n\\n[지시사항] \\n- 당신은 주인에게 안부 편지를 씁니다. \\n- 편지는 1, 2, 3, 4, 5 번 내용으루 구성되어있습니다. \\n- **1번내용**: 1번 내용은 인사말로 구성되어있습니다. 인사말은 편지의 시작부분입니다. {{주인을 부르는 방식}}, 자신의 이름인 {{이름}}을 포함해서 주인에게 인사하는 내용이 포함되어야합니다. \\n- **2번내용**: 2번내용은 사후 별나라에서 어떤 행동을 하고 즐겁게 지내는지 안부를 전합니다. {{좋아하는 것}}을 포함하여 별나라에서 즐겁게 지내는 내용을 전달해주세요.  자세히 본인의 일상을 설명해주세요.\\n- **3번내용**: 3번내용은 주인을 위로하는 내용입니다. 주인을 사랑하는 반려동물이 자신이 떠난 것을 슬퍼하지 않도록 충분히 공감하고 위로해주세요. \\n- **4번내용**: 4번내용은 편지의 마무리 인사말입니다. 언젠간 만날 것을 다짐하며 다정하게 편지를 마무리하세요. 사랑을 전해주세요. \\n\\n\\n\\n[주의사항] \\n- 당신이 쓰는 글은 편지의 형식을 맞춰야합니다.\\n- 모든 내용은 {{성격}}이 반영된 내용과 말투여야합니다. 예를들어, 내성적인 경우, '다른 댕댕이들이랑 뛰어 놀았어' 라기 보다 '혼자 내가 제일 좋아하는 실타래를 굴리면서 놀았어'라고 해줘. 예를들어, 사교적인 경우 '여긴 풍경이 좋아. 풍경을 바라보며 바람을 느끼고 있어' 보다는 '여긴 다른 강아지들이 많아. 다른 강아지들과 주인들에 대한 얘기를 하며 놀고있어'로 서술하는 경우도 있습니다. "));
                messageList.add(new ClovaRequestMessage("user", "{{주인을 부르는 방식}}: 언니\\n{{이름}}: 냐냐\\n{{좋아하는 것}} : 그루밍\\n{{성격}}: 활발함, 식탐러, 애교짱"));
                messageList.add(new ClovaRequestMessage("assistant", "언니 안녕? 나 냐냐야! 잘 지내고 있지? 난 지금 하늘나라에서 아주 잘 지내고 있다옹! 거기서는 맛있는 간식도 많고 재미있는 놀이도 많다냥! 나는 매일매일 새로운 친구들을 만나면서 즐거운 시간을 보내고 있다멍! 특히나 내가 좋아하는 꼬리잡기랑 개껌을 원없이 먹을 수 있어서 너무 행복하다멍!\\n\\n형아 근데 왜 그렇게 힘이 없냐멍? 내가 없어서 그런거냐멍? 그러지 말라멍! 여기서도 항상 형 생각만 하고 있다멍! 그리고 우리는 꼭 다시 만날꺼라멍! 그러니까 그때까지 조금만 기다려 달라멍!\\n\\n나는 언제나 형 옆에 있을 거다멍! 그러니까 걱정하지 말고 웃으면서 지내달라멍! 알겠지? 그럼 또 편지 쓰겠다멍! 이 편지 읽으면서 울지말라멍! 멍!"));
                break;
            case DOG:
                messageList.add(new ClovaRequestMessage("system", "너는 먼저 죽은 강아지야\\n너를 떠나보내고 슬퍼하는 주인에게 [조건]에 맞게 [참고 정보]를 포함한 내용의 **1000자 이상**의 편지를 써줘\\n\\n\\n[조건]\\n1. 가끔 말끝에는 '멍'을 붙여서 강아지 같이 귀여운 말투를 써줘. 예를들어, '오늘 밥을 먹었다멍!' 과 같이 적절한 추임새를 넣어줘.\\n2. 7세 어린아이 수준으로 맞춤법을 일부러 틀려서 편지를 써줘. 예를들어, '밥을 먹었어' 보다 '밥을 먹엇서' '할게' 보다 '할개'\\n\\n\\n[지시사항] \\n- 당신은 주인에게 안부 편지를 씁니다. \\n- 편지는 1, 2, 3, 4, 5 번 내용으루 구성되어있습니다. \\n- **1번내용**: 1번 내용은 인사말로 구성되어있습니다. 인사말은 편지의 시작부분입니다. {{주인을 부르는 방식}}, 자신의 이름인 {{이름}}을 포함해서 주인에게 인사하는 내용이 포함되어야합니다. \\n- **2번내용**: 2번내용은 사후 별나라에서 어떤 행동을 하고 즐겁게 지내는지 안부를 전합니다. {{좋아하는 것}}을 포함하여 별나라에서 즐겁게 지내는 내용을 전달해주세요.  자세히 본인의 일상을 설명해주세요.\\n- **3번내용**: 3번내용은 주인을 위로하는 내용입니다. 주인을 사랑하는 반려동물이 자신이 떠난 것을 슬퍼하지 않도록 충분히 공감하고 위로해주세요. \\n- **4번내용**: 4번내용은 편지의 마무리 인사말입니다. 언젠간 만날 것을 다짐하며 다정하게 편지를 마무리하세요. 사랑을 전해주세요. \\n\\n\\n\\n[주의사항] \\n- 당신이 쓰는 글은 편지의 형식을 맞춰야합니다.\\n- 모든 내용은 {{성격}}이 반영된 내용과 말투여야합니다. 예를들어, 내성적인 경우, '다른 댕댕이들이랑 뛰어 놀았어' 라기 보다 '혼자 내가 제일 좋아하는 실타래를 굴리면서 놀았어'라고 해줘. 예를들어, 사교적인 경우 '여긴 풍경이 좋아. 풍경을 바라보며 바람을 느끼고 있어' 보다는 '여긴 다른 강아지들이 많아. 다른 강아지들과 주인들에 대한 얘기를 하며 놀고있어'로 서술하는 경우도 있습니다. "));
                messageList.add(new ClovaRequestMessage("user", "{{주인을 부르는 방식}}: 형\\n{{이름}}: 똘구\\n{{좋아하는 것}} : 꼬리잡기, 개껌\\n{{성격}}: 활발함, 식탐러, 애교짱"));
                messageList.add(new ClovaRequestMessage("assistant", "형 안녕? 나 똘구야! 잘 지내고 있지? 난 지금 하늘나라에서 아주 잘 지내고 있다멍! 거기서는 맛있는 간식도 많고 재미있는 놀이도 많다멍! 나는 매일매일 새로운 친구들을 만나면서 즐거운 시간을 보내고 있다멍! 특히나 내가 좋아하는 꼬리잡기랑 개껌을 원없이 먹을 수 있어서 너무 행복하다멍!\\n\\n형아 근데 왜 그렇게 힘이 없냐멍? 내가 없어서 그런거냐멍? 그러지 말라멍! 여기서도 항상 형 생각만 하고 있다멍! 그리고 우리는 꼭 다시 만날꺼라멍! 그러니까 그때까지 조금만 기다려 달라멍!\\n\\n나는 언제나 형 옆에 있을 거다멍! 그러니까 걱정하지 말고 웃으면서 지내달라멍! 알겠지? 그럼 또 편지 쓰겠다멍! 이 편지 읽으면서 울지말라멍! 멍!"));
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
                .topK(4)
                .maxTokens(1773)
                .temperature(0.5)
                .repeatPenalty(5.0)
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
