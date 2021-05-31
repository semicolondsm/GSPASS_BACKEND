package com.semicolon.gspass.service.school;

import com.semicolon.gspass.dto.school.MealResponse;
import com.semicolon.gspass.dto.school.RegisterRequest;
import com.semicolon.gspass.dto.school.SchoolResponse;
import com.semicolon.gspass.entity.school.School;
import com.semicolon.gspass.entity.school.SchoolRepository;
import com.semicolon.gspass.exception.ParseErrorException;
import com.semicolon.gspass.exception.SchoolAlreadyExistException;
import com.semicolon.gspass.exception.SchoolNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private static final String MEAL_BASEURL = "https://open.neis.go.kr/hub/mealServiceDietInfo";
    private static final String SCHOOL_BASEURL = "https://open.neis.go.kr/hub/schoolInfo";
    private static final Pattern PATTERN_BRACKET = Pattern.compile("\\([^\\(\\)]+\\)");
    private static final String MENU_PATTERN = "[^\\uAC00-\\uD7AF\\u1100-\\u11FF\\u3130-\\u318F\n]";

    private final SchoolRepository schoolRepository;

    @Override
    public MealResponse getMeals(int schoolId, String date) {
        School school = schoolRepository.findById(schoolId).orElseThrow(SchoolNotFoundException::new);
        Document doc;

        try{
            DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactoty.newDocumentBuilder();
            doc = dBuilder.parse(MEAL_BASEURL + "?ATPT_OFCDC_SC_CODE=" + school.getScCode() + "&SD_SCHUL_CODE=" + school.getSchoolCode() + "&MLSV_YMD=" + date);
        }catch(ParserConfigurationException | SAXException | IOException e){
            throw new ParseErrorException();
        }


        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("row");

        MealResponse response = new MealResponse();

        for(int i=0, length = nList.getLength(); i < length; i++){
            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                List<String> menu = new ArrayList<>();
                String[] menus = deleteBracketTextByPattern(getTagValue("DDISH_NM", element)).split(MENU_PATTERN);

                for(String value : menus){
                    if(value.length() != 0) {
                        menu.add(value);
                    }
                }

                switch (getTagValue("MMEAL_SC_NM", element)){
                    case "조식":
                        response.setBreakfast(menu);
                        break;
                    case "중식":
                        response.setLunch(menu);
                        break;
                    case "석식":
                        response.setDinner(menu);
                        break;
                    default:
                        break;
                }
            }
        }

        return response;
    }

    @Override
    public List<SchoolResponse> getSchools(String name) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        Document doc;
        try{
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            name = URLEncoder.encode(name, "UTF-8");
            doc = dBuilder.parse(SCHOOL_BASEURL + "?SCHUL_NM=" + name);
        }catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParseErrorException();
        }


        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("row");

        List<SchoolResponse> response = new ArrayList<>();

        for(int i=0, length = nList.getLength(); i < length; i++){
            Node node = nList.item(i);
            if(node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                response.add(new SchoolResponse(getTagValue("ATPT_OFCDC_SC_CODE", element), getTagValue("SD_SCHUL_CODE", element),
                        getTagValue("LCTN_SC_NM", element), getTagValue("SCHUL_NM", element)));
            }
        }

        return response;
    }

    @Override
    public String registerSchool(RegisterRequest request) {
        String randomCode = randomCode();
        try{
            schoolRepository.save(
                    School.builder()
                            .schoolCode(request.getSchoolCode())
                            .scCode(request.getScCode())
                            .randomCode(randomCode)
                            .build()
            );
        }catch(Exception e) {
            throw new SchoolAlreadyExistException();
        }

        return randomCode;
    }

    private String randomCode() {
        StringBuilder result = new StringBuilder();
        String[] codes = "QWERTYUIOPASDFGHJKLZXCVBNM0123456789".split("");
        for (int i = 0; i < 6; i++) {
            result.append(codes[new Random().nextInt(codes.length)]);
        }
        return result.toString();
    }

    private String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();

        Node nValue = nodeList.item(0);
        if(nValue == null)
            return "";
        return nValue.getNodeValue();
    }

    private static String deleteBracketTextByPattern(String text) {

        Matcher matcher = PATTERN_BRACKET.matcher(text);

        String pureText = text;
        String removeTextArea;

        while(matcher.find()) {
            int startIndex = matcher.start();
            int endIndex = matcher.end();

            removeTextArea = pureText.substring(startIndex, endIndex);
            pureText = pureText.replace(removeTextArea, "");
            matcher = PATTERN_BRACKET.matcher(pureText);
        }

        return pureText;
    }

}
