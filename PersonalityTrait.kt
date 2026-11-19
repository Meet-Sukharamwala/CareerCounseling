package com.example.data.model

enum class PersonalityTrait(
    val code: String,
    val title: String,
    val shortLabel: String,
    val summary: String,
    val highSchoolFocus: String,
    val colorHex: Long
) {
    REALISTIC(
        code = "R",
        title = "Realistic (Doers)",
        shortLabel = "Realistic",
        summary = "Practical, hands-on, mechanical, outdoor, and technical problem solving.",
        highSchoolFocus = "STEM workshops, robotics, physical sciences, engineering mechanics",
        colorHex = 0xFFEA580C // Warm Rust / Amber Orange
    ),
    INVESTIGATIVE(
        code = "I",
        title = "Investigative (Thinkers)",
        shortLabel = "Investigative",
        summary = "Analytical, research-oriented, scientific inquiry, math, and software logic.",
        highSchoolFocus = "AP Computer Science, advanced calculus, lab sciences, data clubs",
        colorHex = 0xFF2563EB // Royal Blue
    ),
    ARTISTIC(
        code = "A",
        title = "Artistic (Creators)",
        shortLabel = "Artistic",
        summary = "Creative expression, UI/UX, visual design, music, writing, and original ideas.",
        highSchoolFocus = "Digital arts, media production, creative writing, theater, portfolio design",
        colorHex = 0xFF8B5CF6 // Vibrant Purple
    ),
    SOCIAL(
        code = "S",
        title = "Social (Helpers)",
        shortLabel = "Social",
        summary = "Empathetic, teaching, healthcare, counseling, advising, and team development.",
        highSchoolFocus = "Peer tutoring, community volunteer work, psychology, leadership councils",
        colorHex = 0xFF10B981 // Emerald Green
    ),
    ENTERPRISING(
        code = "E",
        title = "Enterprising (Persuaders)",
        shortLabel = "Enterprising",
        summary = "Leadership, entrepreneurship, debate, public speaking, marketing, and business strategy.",
        highSchoolFocus = "DECA / FBLA clubs, student government, speech & debate, startup competitions",
        colorHex = 0xFFF59E0B // Golden Amber
    ),
    CONVENTIONAL(
        code = "C",
        title = "Conventional (Organizers)",
        shortLabel = "Conventional",
        summary = "Organized, detail-oriented, financial data, compliance, systems, and operations.",
        highSchoolFocus = "Accounting, logistics, database management, statistics, yearbook editorship",
        colorHex = 0xFF0284C7 // Sky Blue / Cyan
    );

    companion object {
        fun fromCode(code: String): PersonalityTrait? {
            return entries.find { it.code.equals(code, ignoreCase = true) }
        }
    }
}
