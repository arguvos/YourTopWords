import './App.css';
import {useEffect, useState} from "react";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import ButtonGroup from "@mui/material/ButtonGroup";
import Stack from "@mui/material/Stack";
import {MenuItem, Select} from "@mui/material";


function App() {
    const [word, setWord] = useState("Click know to start");
    const [number, setNumber] = useState(0);
    const [totalCount, setTotalCount] = useState(0);
    const [languages, setLanguages] = useState([]);
    const [language, setLanguage] = useState("");
    const [renderDownload, setRenderDownload] = useState(false);
    const [ready, setReady] = useState(false);

    const next = (value) => {
        let isKnow = new URLSearchParams();
        isKnow.append('isKnow', value);
        fetch("http://localhost:8080/top/next", {
            method: 'POST',
            credentials: 'include',
            body: isKnow
        })
            .then((response) => response.json())
            .then((response) => {
                setWord(response.word);
                setNumber(response.number);
                setRenderDownload(response.finish);
                if (response.finish) {
                    setWord("Finished, for show words click button");
                }
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const prev = () => {
        fetch("http://localhost:8080/top/prev", {
            method: 'POST',
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((response) => {
                setWord(response.word);
                setNumber(response.number);
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const reset = () => {
        fetch("http://localhost:8080/top/reset", {
            method: 'POST',
            credentials: 'include'
        })
            .then((response) => {
                setWord("Click know to start");
                setNumber(0);
                setRenderDownload(false);
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const unknowwords = () => {
        fetch("http://localhost:8080/top/unknowwords", {
            method: 'POST',
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((response) => {
                setWord(response.join(", "));
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const anki = () => {
        fetch("http://localhost:8080/top/anki?" + new URLSearchParams({
                lang: language,
            }), {
            method: 'GET',
            credentials: 'include',
        });
    };

    const getStatus = () => {
        fetch("http://localhost:8080/status", {
            method: 'GET',
            credentials: 'include'
        })
            .then(() => setReady(true))
            .catch(() => {
                setWord("Service is not ready");
            });
    };

    const getCountWords = () => {
        fetch("http://localhost:8080/top/count", {
            method: 'GET',
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((response) => {
                setTotalCount(response);
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const getAvailableLanguage = () => {
        fetch("http://localhost:8080/language/available", {
            method: 'GET',
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((response) => {
                setLanguages(response);
                setLanguage(response[0]);
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    useEffect(() => {
        getStatus();
        getCountWords();
        getAvailableLanguage();
    }, []);

    return (
        <div className="center">
            <Card sx={{minWidth: 300, maxWidth: 300, margin: "auto"}}>
                <CardContent>
                    <Stack direction="row" spacing={2}>
                        <Typography
                            sx={{marginRight: "auto", fontSize: 12}}
                            color="text.secondary"
                            gutterBottom
                        >
                            {number} / {totalCount} word
                        </Typography>
                        <Select sx={{marginLeft: "auto", fontSize: 10}}
                            size="small"
                            labelId="demo-simple-select-autowidth-label"
                            id="demo-simple-select-autowidth"
                            onChange={(e) => setLanguage(e.target.value)}
                            value={language}
                            label="Language"
                        >
                            {languages.map(e => <MenuItem value={e}>{e}</MenuItem>)}
                        </Select>
                        <Button sx={{marginLeft: "auto", fontSize: 10}}
                                variant="outlined"
                                onClick={reset}>reset</Button>
                    </Stack>
                    <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                        Do you know:
                    </Typography>
                    <Typography variant="h5" component="div">
                        {word}
                    </Typography>
                </CardContent>
                {
                    !renderDownload &&
                    <CardActions>
                        <Button sx={{marginRight: "auto"}} variant="contained" onClick={prev}>back</Button>
                        <ButtonGroup
                            disableElevation
                            variant="contained"
                            aria-label="Disabled elevation buttons"
                            sx={{marginLeft: "auto"}}
                        >
                            <Button onClick={() => next(false)}>dont</Button>
                            <Button onClick={() => next(true)}>know</Button>
                        </ButtonGroup>
                    </CardActions>
                }
                {
                    renderDownload &&
                    <CardActions>
                        <Button sx={{margin: "auto", width: "auto"}} variant="contained" color="success" onClick={unknowwords}>
                            SHOW UNKNOW WORDS</Button>
                        <Button sx={{margin: "auto", width: "auto"}} variant="contained" color="success" onClick={anki}>
                            DOWNLOAD ANKI</Button>
                    </CardActions>
                }
            </Card>
        </div>
    );
}

export default App;