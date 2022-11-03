package org.example;

import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class MainController {

    String[] countries = {"Uzbekistan🇺🇿","Kazakhstan🇰🇿","Turkmenistan🇹🇲","Tajikistan🇹🇯","Afghanistan🇦🇫","Turkey🇹🇷","Russian🇷🇺",
            "Kyrgyzstan🇰🇬","China🇨🇳","South Korea🇰🇷","Japan🇯🇵","Australia🇦🇺"};
    String[] organization = {"Uzbekistan MOIA","Kazakhstan MOIA","Turkmenistan MOIA","Tajikistan MOIA","Afghanistan MOIA","Turkey MOIA","Russian MOIA",
            "Kyrgyzstan MOIA","China MOIA","South Korea MOIA","Japan MOIA","Australia MOIA"};
    MyDeportBot myDeportBot;
    Map<Long,Profile> userMap = new LinkedHashMap<>();

    public MainController(MyDeportBot myDeportBot) {
        this.myDeportBot = myDeportBot;
    }

    public void handleMessage(Message message)
    {
        User user = message.getFrom();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(user.getId());
        String text = message.getText();
        if(text.equals("/start") || text.equals("start"))
        {
            userMap.remove(message.getChatId());
            Profile profile = new Profile();
            profile.setUserStep(UserStep.COUNTRY);
            userMap.put(message.getChatId(), profile);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setText("*Enter the country which you want to check deportation:*");
            sendMessage.setReplyMarkup(deportCheckingCountry());
            myDeportBot.send(sendMessage);
        } else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.FULLNAME)) {
            Profile profile = userMap.get(message.getChatId());
            profile.setFullName(text);
            profile.setUserStep(UserStep.SEX);
            sendMessage.setText("Enter your Sex");
            sendMessage.setReplyMarkup(sexButton());
            myDeportBot.send(sendMessage);
        } else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.BIRTHDATE)) {
            Profile profile = userMap.get(message.getChatId());
            profile.setBirthDate(text);
            profile.setUserStep(UserStep.CITIZENSHIP);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setText("*Choose your nationality!*");
            sendMessage.setReplyMarkup(makeCitizenship());
            myDeportBot.send(sendMessage);
        }else if(userMap.get(message.getChatId()).getUserStep().equals(UserStep.CITIZENSHIP)) {
            Profile profile = userMap.get(message.getChatId());
            profile.setCitizenship(text);
            profile.setUserStep(UserStep.DOCUMENTTYPE);
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
            remove.setRemoveKeyboard(true);
            sendMessage.setReplyMarkup(remove);
            sendMessage.setText("Removed Replykeyboardmarkup");
            myDeportBot.send(sendMessage);
            sendMessage.setText("Enter your Document type");
            sendMessage.setReplyMarkup(typeofDocument());
            myDeportBot.send(sendMessage);

        }else if(userMap.get(message.getChatId()).getUserStep().equals(UserStep.ISSUINGCOUNTRYORORGANIZATION)) {
            Profile profile = userMap.get(message.getChatId());
            profile.setIssuingCountryOrOrganization(text);
            profile.setUserStep(UserStep.REASONOFCHECK);
            ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
            remove.setRemoveKeyboard(true);
            sendMessage.setReplyMarkup(remove);
            sendMessage.setText("Removed Replykeyboardmarkup");
            myDeportBot.send(sendMessage);
            sendMessage.setText("why are you  checking deport?");
            sendMessage.setReplyMarkup(reasonOfcheckingDeport());
            myDeportBot.send(sendMessage);
        }else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.DOCNUMBER)) {
            Profile profile = userMap.get(message.getChatId());
            profile.setDocNumber(text);
            profile.setUserStep(UserStep.VALIDITY);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setText("*Enter your document's expiration date* \n" +
                                "*Example* : Year,Month,Day (2030,8,15)\"");
            myDeportBot.send(sendMessage);
        }else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.VALIDITY)) {
            Profile profile = userMap.get(message.getChatId());
            profile.setValidity(text);
            profile.setUserStep(UserStep.FINISH);
            sendMessage.setText("Do want to take your all information about deportation?");
            sendMessage.setReplyMarkup(yesOrNo());
            myDeportBot.send(sendMessage);}

    }

    public void handleCallbackQuery(CallbackQuery callbackQuery) throws IOException {
        Message message = callbackQuery.getMessage();
        String query = callbackQuery.getData();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
        if(userMap.get(message.getChatId()).getUserStep().equals(UserStep.COUNTRY)){
            Profile profile = userMap.get(message.getChatId());
            profile.setCountry(query);
            profile.setUserStep(UserStep.FULLNAME);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setText("*Eneter your Full Name:* \n" +
                    "*Example* : Name,Surname,Middle Name (if you have middle name enter it.)");
            myDeportBot.send(sendMessage);
        } else if(userMap.get(message.getChatId()).getUserStep().equals(UserStep.SEX))
        {
            Profile profile = userMap.get(message.getChatId());
            profile.setSex(query);
            profile.setUserStep(UserStep.BIRTHDATE);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setText("*Eneter your Birth Date:* \n" +
                    "*Example* : Year,Month,Day (1999,4,27)");
            myDeportBot.send(sendMessage);

        } else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.DOCUMENTTYPE)){
        Profile profile = userMap.get(message.getChatId());
        profile.setDocumentType(query);
        profile.setUserStep(UserStep.ISSUINGCOUNTRYORORGANIZATION);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setText("*Choose issuing Country or Organization*");
        sendMessage.setReplyMarkup(issuingCountry());
        myDeportBot.send(sendMessage);
        }else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.REASONOFCHECK)){
            Profile profile = userMap.get(message.getChatId());
            profile.setReasonOfcheck(query);
            profile.setUserStep(UserStep.DOCNUMBER);
            sendMessage.setParseMode(ParseMode.MARKDOWN);
            sendMessage.setText("*Enter the Document number:* \n" +
                                "*Here is Example:* AA1234567");
            myDeportBot.send(sendMessage);
        } else if (userMap.get(message.getChatId()).getUserStep().equals(UserStep.FINISH)) {
            Profile profile = userMap.get(message.getChatId());
            if(query.equals("yes"))
            {
                File file = new File("test.txt");
                file.createNewFile();
                PrintWriter pw = new PrintWriter(file);
                sendMessage.setParseMode(ParseMode.MARKDOWN);
                String info = "*Your full name is: *" +profile.getFullName() +"\n\n"+
                              "*Your sex is: *" +profile.getSex()+"\n\n"+
                              "*Your birth date is: *" +profile.getBirthDate()+"\n\n"+
                              "*Your citizenship is: *" +profile.getCitizenship()+"\n\n"+
                              "*Your document type is: *"+profile.getDocumentType()+"\n\n"+
                              "*Issuing county or Organization is: *"+profile.getIssuingCountryOrOrganization()+"\n\n"+
                              "*Reason of checking deport is: *"+profile.getReasonOfcheck()+"\n\n"+
                              "*Your document number is: *"+profile.getDocNumber()+"\n\n"+
                              "*Validation of the document is: *"+profile.getValidity()+"\n\n\n"+
                              "*My recommendation is type the Google.com uncle: *(" +profile.getCountry()+" check deportation"+
                              ") then enter above information to the useful web links 😂😂😂😂";
                sendMessage.setText(info);

                  pw.print(info);
                  pw.close();
                myDeportBot.send(sendMessage);
            }else
            {
                sendMessage.setText("Batar bo'lll 😝😝😝😝😝");
            }

        }
    }

    public InlineKeyboardMarkup sexButton()
    {
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        InlineKeyboardButton btnMale = new InlineKeyboardButton("Male");
        btnMale.setCallbackData("Male");
        row1.add(btnMale);

        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton btnFemale = new InlineKeyboardButton("Female");
        btnFemale.setCallbackData("Female");
        row2.add(btnFemale);

        List<InlineKeyboardButton> row3 = new LinkedList<>();
        InlineKeyboardButton btnUnisex = new InlineKeyboardButton("Unisex");
        btnUnisex.setCallbackData("Unisex");
        row3.add(btnUnisex);

        List<List<InlineKeyboardButton>> rowCol = Arrays.asList(row1,row2,row3);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(rowCol);


        return keyboard;

    }
    public ReplyKeyboardMarkup makeCitizenship()
    {
        int count = 0;
        List<KeyboardRow> rowList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j <4 ; j++) {
                KeyboardButton button = new KeyboardButton();
                button.setText(countries[count++]);
                row.add(button);
            }
            rowList.add(row);
        }
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowList);
         return keyboardMarkup;
    }

    public InlineKeyboardMarkup typeofDocument()
    {
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        InlineKeyboardButton btnMale = new InlineKeyboardButton("Id card");
        btnMale.setCallbackData("Id card");
        row1.add(btnMale);

        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton btnFemale = new InlineKeyboardButton("Biometric passport");
        btnFemale.setCallbackData("Biometric passport");
        row2.add(btnFemale);

        List<InlineKeyboardButton> row3 = new LinkedList<>();
        InlineKeyboardButton btnUnisex = new InlineKeyboardButton("National passport");
        btnUnisex.setCallbackData("National passport");
        row3.add(btnUnisex);

        List<List<InlineKeyboardButton>> rowCol = Arrays.asList(row1,row2,row3);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(rowCol);


        return keyboard;

    }

    public ReplyKeyboardMarkup issuingCountry()
    {
        int count = 0;
        List<KeyboardRow> rowList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            KeyboardRow row = new KeyboardRow();
            for (int j = 0; j <4 ; j++) {
                KeyboardButton button = new KeyboardButton();
                button.setText(organization[count++]);
                row.add(button);
            }
            rowList.add(row);
        }
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setKeyboard(rowList);
        return keyboardMarkup;
    }
    public InlineKeyboardMarkup reasonOfcheckingDeport()
    {
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        InlineKeyboardButton btnMale = new InlineKeyboardButton("Work");
        btnMale.setCallbackData("work");
        row1.add(btnMale);

        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton btnFemale = new InlineKeyboardButton("Travel");
        btnFemale.setCallbackData("travel");
        row2.add(btnFemale);

        List<InlineKeyboardButton> row3 = new LinkedList<>();
        InlineKeyboardButton btnUnisex = new InlineKeyboardButton("Other");
        btnUnisex.setCallbackData("other");
        row3.add(btnUnisex);

        List<List<InlineKeyboardButton>> rowCol = Arrays.asList(row1,row2,row3);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(rowCol);


        return keyboard;
    }
    public InlineKeyboardMarkup yesOrNo()
    {
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        InlineKeyboardButton btnMale = new InlineKeyboardButton("Yes");
        btnMale.setCallbackData("yes");
        row1.add(btnMale);

        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton btnFemale = new InlineKeyboardButton("No");
        btnFemale.setCallbackData("no");
        row2.add(btnFemale);

        List<List<InlineKeyboardButton>> rowCol = Arrays.asList(row1,row2);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(rowCol);


        return keyboard;
    }

    public InlineKeyboardMarkup deportCheckingCountry()
    {
        List<InlineKeyboardButton> row1 = new LinkedList<>();
        InlineKeyboardButton btnMale = new InlineKeyboardButton("Russia🇷🇺");
        btnMale.setCallbackData("Russia");
        row1.add(btnMale);

        List<InlineKeyboardButton> row2 = new LinkedList<>();
        InlineKeyboardButton btnFemale = new InlineKeyboardButton("Turkey🇹🇷");
        btnFemale.setCallbackData("Turkey");
        row2.add(btnFemale);

        List<InlineKeyboardButton> row3 = new LinkedList<>();
        InlineKeyboardButton btn = new InlineKeyboardButton("United Arab Emirates🇦🇪(Dubai)");
        btn.setCallbackData("United Arab Emirates");
        row3.add(btn);

        List<InlineKeyboardButton> row4 = new LinkedList<>();
        InlineKeyboardButton btnUnisex = new InlineKeyboardButton("China🇨🇳");
        btnUnisex.setCallbackData("China");
        row4.add(btnUnisex);

        List<InlineKeyboardButton> row5 = new LinkedList<>();
        InlineKeyboardButton btn1 = new InlineKeyboardButton("South Korea🇰🇷");
        btn1.setCallbackData("South Korea");
        row5.add(btn1);


        List<List<InlineKeyboardButton>> rowCol = Arrays.asList(row1,row2,row3,row4,row5);
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup(rowCol);


        return keyboard;
    }
}
