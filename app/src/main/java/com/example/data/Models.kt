package com.example.data

data class Lesson(
    val index: Int,
    val title: String,
    val duration: String,
    val videoUrl: String, // Simulate play
    val summary: String
)

data class Course(
    val id: String,
    val title: String,
    val category: String, // AI, Salesforce, Full Stack, Data Science, DevOps, Digital Marketing
    val description: String,
    val duration: String,
    val mode: String, // Online + Offline Hybrid
    val trainer: String,
    val lessons: List<Lesson>,
    val imageUrl: String = ""
)

data class LiveClass(
    val id: String,
    val topic: String,
    val courseTitle: String,
    val trainer: String,
    val dateString: String, // e.g., "Today, 6:30 PM"
    val duration: String,
    val zoomLink: String,
    val isLive: Boolean = false
)

data class JobOpening(
    val id: String,
    val title: String,
    val company: String,
    val location: String, // Hyderabad locations
    val type: String, // Full-time, Internship
    val salary: String,
    val requirements: List<String>,
    val skillsNeeded: List<String>,
    val description: String
)

data class StudentLeaderboardEntry(
    val name: String,
    val xp: Int,
    val rank: Int,
    val isCurrentUser: Boolean = false
)

object BrollyData {
    val CoursesList = listOf(
        Course(
            id = "ai_ml",
            title = "Generative AI & Machine Learning Masters",
            category = "AI Courses",
            description = "Master LLMs, Prompt Engineering, PyTorch, and AI Orchestration. Includes building production-ready AI agents and custom fine-tuning projects.",
            duration = "12 Weeks",
            mode = "Hybrid (Madhapur Campus)",
            trainer = "Dr. Srikanth M. (ex-Google AI researcher)",
            lessons = listOf(
                Lesson(0, "Introduction to Deep Learning & PyTorch", "45 mins", "vid_ai_1", "Understand neural networks, activation functions, and PyTorch tensors."),
                Lesson(1, "LLM Architectures & Transformers", "50 mins", "vid_ai_2", "Dive deep into attention mechanisms, self-attention, and the GPT architecture."),
                Lesson(2, "Prompt Engineering & RAG Systems", "60 mins", "vid_ai_3", "Learn advanced system prompt patterns, few-shot prompting, and Retrieval Augmented Generation (RAG)."),
                Lesson(3, "Agentic AI & LangChain Framework", "55 mins", "vid_ai_4", "Build autonomous agents that utilize tool-calling and external memory databases.")
            )
        ),
        Course(
            id = "salesforce",
            title = "Salesforce Certified Admin & Developer (Apex)",
            category = "Salesforce",
            description = "Complete hands-on training for Salesforce Administrator and Platform Developer I. Learn Apex Programming, Lightning Web Components (LWC), and flows.",
            duration = "10 Weeks",
            mode = "On-Campus (Ameerpet Centre)",
            trainer = "K. Rajesh (Senior Salesforce Architect)",
            lessons = listOf(
                Lesson(0, "Salesforce CRM Foundations & Schema Builder", "40 mins", "vid_sf_1", "Master objects, relationships, fields, and record types."),
                Lesson(1, "Process Automation using Flows", "50 mins", "vid_sf_2", "Automate business guidelines using Salesforce Flow builder interactively."),
                Lesson(2, "Apex Language Essentials", "55 mins", "vid_sf_3", "Learn SOQL queries, Apex triggers, and DML operations safely."),
                Lesson(3, "Lightning Web Components Introduction", "65 mins", "vid_sf_4", "Build responsive modern interfaces using HTML, JS, and CSS in SFDX.")
            )
        ),
        Course(
            id = "fullstack_java",
            title = "Full Stack Java Web Development",
            category = "Full Stack",
            description = "Become a highly employed Java Full Stack developer. Learn core Java, Spring Boot, Hibernate, MySQL, and React frontend development.",
            duration = "16 Weeks",
            mode = "Hybrid Classroom",
            trainer = "Anish Kumar (Core Platform Architect)",
            lessons = listOf(
                Lesson(0, "Java OOPs Basics & Collections", "42 mins", "vid_java_1", "Encapsulation, inheritance, polymorphism, and lists/maps structures."),
                Lesson(1, "Building REST APIs with Spring Boot", "50 mins", "vid_java_2", "Set up controllers, dependency injection, and JPA database connections."),
                Lesson(2, "React Essentials & Hooks", "48 mins", "vid_java_3", "Learn JSX, state, props, and fetching backing endpoints in React."),
                Lesson(3, "Microservices & Deployment Containerization", "60 mins", "vid_java_4", "Deploy Spring Boot apps to AWS using Docker and Eureka Naming Server.")
            )
        ),
        Course(
            id = "devops",
            title = "AWS & DevOps Cloud Masterclass",
            category = "DevOps",
            description = "Build reliable CI/CD pipelines and orchestrate cloud applications. Learn Linux, Docker, Ansible, Kubernetes, Jenkins, and AWS solutions.",
            duration = "10 Weeks",
            mode = "Online Live",
            trainer = "Vikram Dev (Lead DevOps SRE)",
            lessons = listOf(
                Lesson(0, "Linux Scripting & Networking", "38 mins", "vid_devops_1", "Master bash commands, user permissions, and TCP/IP layouts."),
                Lesson(1, "Dockerizing Application Services", "45 mins", "vid_devops_2", "Compose Dockerfiles, manage volumes, and launch container clusters."),
                Lesson(2, "Kubernetes Clustering & Scaling", "55 mins", "vid_devops_3", "Configure Pods, Deployments, Services, and ingress networks."),
                Lesson(3, "CI/CD Orchestration with Gitlab & Jenkins", "50 mins", "vid_devops_4", "Automate testing, container building, and staging deploys.")
            )
        ),
        Course(
            id = "datascience",
            title = "Data Science, Tableau & Power BI",
            category = "Data Science",
            description = "Learn statistical data analysis, Python cleaning, and premium reporting interfaces with Tableau and Power BI.",
            duration = "14 Weeks",
            mode = "On-Campus (Madhapur Lab)",
            trainer = "Meera Nair (Chief Data Scientist)",
            lessons = listOf(
                Lesson(0, "Exploratory Data Analysis using Pandas", "44 mins", "vid_ds_1", "Data ingestion, cleaning, NaN replacement, and descriptive statistics."),
                Lesson(1, "Data Presentation with Tableau Prep", "48 mins", "vid_ds_2", "Building sheets, dashboards, filter paths, and maps configurations."),
                Lesson(2, "Power BI DAX Formula Essentials", "50 mins", "vid_ds_3", "Write calculations, measures, calendar tables, and visual matrices."),
                Lesson(3, "Predictive Modelling with Scikit-Learn", "60 mins", "vid_ds_4", "Train Linear Regression, Decision Trees, and evaluate metrics.")
            )
        )
    )

    val LiveClassesList = listOf(
        LiveClass(
            id = "live_1",
            topic = "Deploying Spring Boot to AWS EC2",
            courseTitle = "Full Stack Java Web Development",
            trainer = "Anish Kumar",
            dateString = "Today, 6:30 PM (IST)",
            duration = "1.5 Hours",
            zoomLink = "https://zoom.us/test",
            isLive = true
        ),
        LiveClass(
            id = "live_2",
            topic = "LWC Component Communication Patterns",
            courseTitle = "Salesforce Certified Admin & Developer",
            trainer = "K. Rajesh",
            dateString = "Tomorrow, 5:00 PM (IST)",
            duration = "2 Hours",
            zoomLink = "https://meet.google.com",
            isLive = false
        ),
        LiveClass(
            id = "live_3",
            topic = "Evaluating RAG Performance with Ragas",
            courseTitle = "Generative AI & Machine Learning Masters",
            trainer = "Dr. Srikanth M.",
            dateString = "May 25, 7:00 PM (IST)",
            duration = "1 Hour",
            zoomLink = "https://zoom.us/test",
            isLive = false
        )
    )

    val JobOpenings = listOf(
        JobOpening(
            id = "job_1",
            title = "Junior Java Full Stack Developer (Immediate Joiner)",
            company = "Virtusa Bangalore & Hyderabad",
            location = "Hyderabad (Hitec City)",
            type = "Full-time",
            salary = "₹4.5 - ₹6.5 LPA",
            requirements = listOf(
                "Good knowledge of Java SE, Oops, and Collections.",
                "Familiarity with Spring Boot REST Controllers and MySQL databases.",
                "Basic understanding of HTML, CSS, React, or Angular is a plus.",
                "Excellent debugging and analytical skills."
            ),
            skillsNeeded = listOf("Java", "Spring Boot", "REST APIs", "SQL", "React"),
            description = "Excellent entry-level job for Brolly Academy freshers. You will build and optimize user-facing Spring REST layouts."
        ),
        JobOpening(
            id = "job_2",
            title = "Salesforce Development Intern",
            company = "Capgemini India",
            location = "Hyderabad (Gachibowli Campus)",
            type = "Internship",
            salary = "₹25,000 / month",
            requirements = listOf(
                "Completed Salesforce ADM-201 and Platform Developer 1 training structures.",
                "Good command over custom workflow flows, validation policies, and security settings.",
                "Basic exposure to Apex coding controllers and SOQL queries.",
                "Excellent conversational skills."
            ),
            skillsNeeded = listOf("Salesforce", "Apex Triggers", "Flows", "Lightning components"),
            description = "Great internship path to full-time absorption! Enhance cloud solutions for enterprise-grade clients."
        ),
        JobOpening(
            id = "job_3",
            title = "Generative AI Developer",
            company = "Cognizant Technology Solutions",
            location = "Hyderabad (Madhapur Sector)",
            type = "Full-time",
            salary = "₹8.0 - ₹12.0 LPA",
            requirements = listOf(
                "Strong Python skills and PyTorch model operations.",
                "Experience building RAG orchestrations using LangChain or LlamaIndex.",
                "Familiarity with Gemini API tool-calling and parameter configurations.",
                "Understanding of vector indexes (e.g., Pinecone, ChromaDB)."
            ),
            skillsNeeded = listOf("Python", "LangChain", "Gemini API", "Pinecone", "RAG"),
            description = "Design and maintain custom backend generative AI agents for intelligent search, summaries, and chat utilities."
        ),
        JobOpening(
            id = "job_4",
            title = "Cloud Operations & SRE Associate",
            company = "Tech Mahindra",
            location = "Hyderabad (Bahadurpally)",
            type = "Full-time",
            salary = "₹5.0 - ₹7.0 LPA",
            requirements = listOf(
                "Excellent understanding of Linux networking and bash coding.",
                "Hands-on practice containerizing with Docker Compose templates.",
                "Basic understanding of Cloud orchestration or Terraform scripts.",
                "Willingness to handle high system availability shifts."
            ),
            skillsNeeded = listOf("Linux", "Docker", "AWS", "CI/CD Pipelines", "Jenkins"),
            description = "Maintain AWS-bound production clusters and guarantee fast integration."
        )
    )

    val Leaderboard = listOf(
        StudentLeaderboardEntry("Sai Kumar (You)", 1240, 4, isCurrentUser = true),
        StudentLeaderboardEntry("Vikram Kumar", 1850, 1),
        StudentLeaderboardEntry("Pranitha Reddy", 1620, 2),
        StudentLeaderboardEntry("Harsha Vardhan", 1400, 3),
        StudentLeaderboardEntry("Deepika Rao", 1120, 5),
        StudentLeaderboardEntry("Satish Raju", 980, 6)
    )
}
