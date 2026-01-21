import React from "react";

const CoursesOffered = () => {
  return (
    <section className="py-10 px-4 max-w-5xl mx-auto">
      <h2 className="text-3xl font-bold mb-6 text-center">
        Courses Offered – Nethaji Degree College, Sircilla
      </h2>
      <p className="mb-6 text-center text-gray-700 italic">
        (Affiliated to Satavahana University)
      </p>
      <p className="mb-6 text-gray-800">
        We offer a diverse range of undergraduate programs that blend academic
        excellence with industry relevance, preparing students for emerging
        career opportunities.
      </p>

      <ul className="space-y-6 text-gray-800">
        <li>
          🎓 <strong>B.Sc (Data Science):</strong> Focused on data analysis,
          programming, machine learning, and statistical methods, this course
          prepares students for the growing field of data-driven
          decision-making.
        </li>
        <li>
          🎓 <strong>B.Sc (Food Science):</strong> Covers food chemistry,
          nutrition, food safety, quality control, and processing techniques,
          ideal for students interested in the food industry, research, and
          public health.
        </li>
        <li>
          🎓 <strong>B.Sc (M.P.Cs – Mathematics, Physics, Computer Science):</strong>{" "}
          Provides a strong foundation in core sciences and computer
          applications, preparing students for careers in IT, education,
          research, and more.
        </li>
        <li>
          🎓 <strong>B.Com (Business Analytics):</strong> Integrates commerce
          fundamentals with analytical tools, business intelligence, and
          decision-making skills suited for modern business environments.
        </li>
        <li>
          🎓 <strong>B.Com (Computer Applications):</strong> Combines commerce
          with practical computer knowledge like accounting software, databases,
          and programming, equipping students for roles in finance, IT, and
          administration.
        </li>
        <li>
          🎓 <strong>BBA (Artificial Intelligence):</strong> Blends core
          management education with AI concepts, machine learning, and business
          automation, aimed at developing tech-savvy future leaders.
        </li>
      </ul>
    </section>
  );
};

export default CoursesOffered;
