package com.example.interestplanet.service;

import com.example.interestplanet.model.Article;
import com.example.interestplanet.model.Planet;
import com.example.interestplanet.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DataInitService {
    public static void Init() {
        ServiceRegister.UserServiceInstance.drop();
        ServiceRegister.PlanetServiceInstance.drop();
        ServiceRegister.ArticleServiceInstance.drop();
        ServiceRegister.UserArticleAttitudeServiceInstance.drop();
        ServiceRegister.ArticleCommentServiceInstance.drop();

        String pwd = ServiceRegister.UserServiceInstance.encryptPwd("123456");
        User admin = new User("admin", pwd, "admin", "");
        User jack = new User("jack", pwd, "I'm Jack", "");
        User michael = new User("michael", pwd, "I'm Michael", "");
        ServiceRegister.UserServiceInstance.addOrUpdate(admin);
        ServiceRegister.UserServiceInstance.addOrUpdate(jack);
        ServiceRegister.UserServiceInstance.addOrUpdate(michael);

        Planet sport = new Planet(jack.getId(), "images/sport_cover.png", "Sport Club", new ArrayList<String>() {{add(jack.getId());}});
        Planet music = new Planet(michael.getId(), "images/music_cover.png", "Music Club", new ArrayList<String>() {{add(michael.getId());}});

        ServiceRegister.PlanetServiceInstance.addOrUpdate(sport);
        ServiceRegister.PlanetServiceInstance.addOrUpdate(music);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = formatter.format(new Date(System.currentTimeMillis()));
        Article spotArticle1 = new Article(sport.getId(), jack.getId(), "images/sport1.png",
                "How Quickly Do You Lose Strength?",
                "Muscle mass loss, which might limit your ability to lift weights or lug home your groceries, isn’t likely to be significantly impacted in two to three weeks time, though this too depends on several factors, including age, diet, sleep hygiene, and your fitness level before you take a break, says Campbell. He points to a study published in May 2020 in the International Journal of Exercise Science in which researchers found that even three weeks of detraining doesn’t decrease muscle thickness, strength, or performance in sports in a group of 21 male adolescent athletes.\n" +
                        "\n" +
                        "An older study suggested that age is a huge factor when it comes to losing (or not losing) muscle strength. Researchers looked at the effects of strength training and detraining on adults in two age groups: 20- to 30-year-olds (18 participants) and 65- to 75-year-olds (23 participants). After nine weeks of resistance training, all participants increased their one-repetition maximum strength (the younger group by 34 percent, and the older group by 28 percent). After 31 weeks of detraining, the younger adults lost just 8 percent of the strength they had gained, while the older adults lost 14 percent. According to the study, loss of strength happens more quickly as we age, although it decreases far slower than cardiovascular fitness.",
                now);
        Article spotArticle2 = new Article(sport.getId(), jack.getId(), "images/sport2.png",
                "How Much Exercise Is Enough?",
                "Many exercise institutions, such as the American Council on Exercise (ACE), the American College of Sports Medicine (ACSM), the National Academy of Sports Medicine (NASM), and the National Strength and Conditioning Association (NSCA) certify personal trainers. Once certified, many of these groups require the completion of continuing education credits, holding special insurance, and taking regular CPR-AED classes in order for trainers to maintain their certification and licenses. The National Commission for Certifying Agencies (NCCA), the gold standard of accrediting bodies, currently backs more than a dozen fitness professional certifications, including those from these institutions.\n" +
                        "\n" +
                        "Personal-training certifications include “certified personal trainer” (CPT), which readies someone for general exercise instruction; “certified strength and conditioning specialist” (CSCS), which focuses on resistance training for everyday and professional athletes; “corrective exercise specialist” (CES), which focuses on exercises to help improve movement dysfunctions and imbalances; and “certified exercise physiologist” (CEP), which focuses on training someone on how to analyze people’s fitness to help them improve their health or maintain good health.",
                now);
        Article musicArticle1 = new Article(music.getId(), michael.getId(), "images/music1.png",
                "Why We Love Music",
                "Music impacts us in ways that other sounds don’t, and for years now, scientists have been wondering why. Now they are finally beginning to find some answers. Using fMRI technology, they’re discovering why music can inspire such strong feelings and bind us so tightly to other people.\n" +
                        "\n" +
                        "“Music affects deep emotional centers in the brain, “ says Valorie Salimpoor, a neuroscientist at McGill University who studies the brain on music. “A single sound tone is not really pleasurable in itself; but if these sounds are organized over time in some sort of arrangement, it’s amazingly powerful.”",
                now);

        ServiceRegister.ArticleServiceInstance.addOrUpdate(spotArticle1);
        ServiceRegister.ArticleServiceInstance.addOrUpdate(spotArticle2);
        ServiceRegister.ArticleServiceInstance.addOrUpdate(musicArticle1);
    }
}
