import * as React from "react";
import customAxios from "../utils/MyAxios";
import Constants from "../Constants";
import CommonFunctions from "../services/CommonFunction";
import Card from "@mui/material/Card";
import CardContent from "@mui/material/CardContent";
import Typography from "@mui/material/Typography";
import { CircularProgress } from "@mui/material";

export default function IssueListing() {
  const commFunc = new CommonFunctions();
  const [issues, setIssue] = React.useState([]);
  const [user_id] = React.useState(window.localStorage.getItem("user_id"));
  const [isLoading, setIsLoading] = React.useState(true);

  const renderIssues = (issue, index) => (
    <Card key={index} sx={{ minWidth: 275, marginBottom: 2 }}>
      <CardContent>
        <Typography variant="h6" component="div">
          Issue Reference ID: {issue.id}
        </Typography>
        <hr></hr>
        <Typography variant="body1" color="text.secondary">
          Description: {issue.description}
        </Typography>
      </CardContent>
    </Card>
  );

  React.useEffect(() => {
    customAxios.get(Constants.GETISSUES).then(
      (res) => {
        const filteredIssues = res.data.filter(
          (issue) => issue.user.user_id.toString() === user_id
        );
        setIssue(filteredIssues);
        setIsLoading(false);
      },
      (error) => {
        console.error(error);

        commFunc.showAlertMessage(
          "Error while fetching issues",
          "error",
          3000,
          "bottom"
        );
        setIsLoading(false);
      }
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <div style={{ margin: "2rem auto", width: "80%" }}>
      {isLoading ? (
        <>
          <center style={{ marginTop: "3rem" }}>
            <CircularProgress></CircularProgress>
            <h4>Loading issues reported by you</h4>
          </center>
        </>
      ) : (
        <>
          {issues.length > 0 ? (
            <div>{issues.map(renderIssues)}</div>
          ) : (
            <Typography variant="h6" component="div" textAlign="center">
              No issues reported
            </Typography>
          )}
        </>
      )}
    </div>
  );
}
